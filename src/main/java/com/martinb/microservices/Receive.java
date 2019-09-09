package com.martinb.microservices;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Receive {

    private static final String QUEUE_NAME = "hello";

    @Bean
    CommandLineRunner setupReceiver() {
        return args -> {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");

                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);

                log.info("[!]: Awaiting for messages...");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        log.info("[-]: Message Received: '" + message + "'");
                    }
                };

                channel.basicConsume(QUEUE_NAME, true, consumer);
            } catch(Exception e) {
                e.printStackTrace();
            }
        };
    }
}
