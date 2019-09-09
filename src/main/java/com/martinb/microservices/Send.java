package com.martinb.microservices;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "Hello World!";

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));

            log.debug("[+]: Message sent to the queue.");

            channel.close();
            connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
