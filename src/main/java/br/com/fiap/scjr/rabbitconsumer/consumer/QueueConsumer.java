package br.com.fiap.scjr.rabbitconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;

import br.com.fiap.scjr.rabbitconsumer.model.DroneModel;

@Component
public class QueueConsumer {

    private static final Logger log = LoggerFactory.getLogger(QueueConsumer.class);

    @RabbitListener(queues = {"${queue.fiap}"})
    public void receiveMessage(@Payload String fileBody){
    	DroneModel drone = new Gson().fromJson(fileBody, DroneModel.class);

    		log.info("Mensagem recebida:" + drone.getIdDrone());

    	
    }
}
