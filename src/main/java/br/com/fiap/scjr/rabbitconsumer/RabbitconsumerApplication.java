package br.com.fiap.scjr.rabbitconsumer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class RabbitconsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitconsumerApplication.class, args);
	}

}
