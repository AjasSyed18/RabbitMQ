package com.example.rabbitMQnKafka.consumer;

import com.example.rabbitMQnKafka.Helper.RabbitMqProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class RabbitmqConsumer {

    /*@Value("${rabbit.service.queueA}")
    public String queueA;

    @Value("${rabbit.service.queueB}")
    public String queueB;*/

    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    /*@RabbitListener(queues = "#{rabbitMqProperties.getQueueA()}")
    public Message receiveMessage(String message) {
        log.info("Consuming message from rabbitMq:::::");
        try {
            log.info("Received message: {}", message);

            byte[] messageBody = message.getBytes();

            return MessageBuilder.withBody(messageBody)
                    .setContentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();


        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
            return null;
        }
    }*/

    /*@RabbitListener(queues = "${rabbitMqProperties.getQueueA}")
    public void receiveMessage(String message) {
        log.info("Consuming message from rabbitMq:::::");
        try {
            log.info("Received message: {}", message);
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }*/


    @RabbitListener(id = "consumerTopicA", queues = "${rabbit.service.queueA}")
    public void consumeMsgNorth(String message) {
        try {
            log.info("Received message: {} from queue -> 'north'", message);
           /* byte[] bytes = message.getBody();
            String bytesString = bytes.toString();
            log.info("Message received -> {} from queue -> 'north'", bytesString);*/
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "consumerTopicB", queues = "${rabbit.service.queueB}")
    public void consumeMsgSouth(String message) {
        try {
            log.info("Received message: {} from queue -> 'south'", message);
           /* byte[] bytes = message.getBody();
            String bytesString = bytes.toString();
            log.info("Message received -> {} from queue -> 'north'", bytesString);*/
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

}
