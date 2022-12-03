package br.com.fiap.scjr.rabbitconsumer.consumer;

import br.com.fiap.scjr.rabbitconsumer.validator.DroneValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import br.com.fiap.scjr.rabbitconsumer.model.DroneModel;

import java.util.List;

@Component
public class QueueConsumer {

    @Autowired
    private JavaMailSender mailSender; //Para pegar as configuracoes no application.yml
    private static final Logger log = LoggerFactory.getLogger(QueueConsumer.class);
    private DroneValidator droneValidator = new DroneValidator();//Cria uma instancia para da classe de validacao
    private Gson gson = new Gson();//Cria uma instancia do GSON para converter as mensagens para objetos java

    @RabbitListener(queues = {"${queue.fiap.nome}"}, containerFactory = "consumerBatchContainerFactory")//Configurei o listener para ele funcionar com mensagens em lote, o containerFactory eh o metodo consumerBatchContainerFactory da classe QueueConfiguration
    public void receiveMessage(@Payload List<String> messages){
        try {
            //Pega as a lista de mensagens e faz um foreach para processar cada mensagem
            for (String message: messages) {
                DroneModel drone = gson.fromJson(message, DroneModel.class);//Converte a mensagen de Json para objeto java
                if (!droneValidator.validar(drone)) {//Valida se ha alguma temperatura ou umidade inconsistente, se estiver inconsistente, envia email
                    log.info(sendMailDrone(drone));
                }
            }
            Thread.sleep(60000);//Trava a thread por 1min, para depois disso executar ela novamente. Tentei usar o Scheduler do Springboot, mas nao consegui :'(
        } catch (InterruptedException e) {
            log.info("Problemas na thread: " + e.getMessage());
        }
    }

    public String sendMailDrone(DroneModel drone) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Drone " + drone.getIdDrone() + " detectou problemas");
        message.setText("Temperatura: " + drone.getTemperatura() + ", umidade: " + drone.getUmidade() + ", latitude: " + drone.getLatitude()
                            + ", longitude: " + drone.getLongitude());
        message.setTo("descobreaebobo@hotmail.com");
        message.setFrom("descobreaebobo@hotmail.com");
        try {
            mailSender.send(message);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }
}
