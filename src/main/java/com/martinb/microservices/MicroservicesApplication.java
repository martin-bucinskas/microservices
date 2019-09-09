package com.martinb.microservices;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroservicesApplication {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) {

		ConnectionFactory factory = new ConnectionFactory();

		SpringApplication.run(MicroservicesApplication.class, args);
	}

}
