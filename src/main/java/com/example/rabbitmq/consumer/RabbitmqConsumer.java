package com.example.rabbitmq.consumer;

import com.example.rabbitmq.Helper.RabbitMqProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
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

    @RabbitListener(id = "directConsumer1", queues = "${rabbit.service.direct.queue-north}")
    public void consumeDirectNorthMessage(Message message) {
        try {
            log.info("queue -> 'north', Received message: {}", message);
            byte[] messageBody = message.getBody();
            String actualMsgStr = new String(messageBody);
            log.info("Message -> {}", actualMsgStr);
           /* byte[] bytes = message.getBody();
            String bytesString = bytes.toString();
            log.info("Message received -> {} from queue -> 'north'", bytesString);*/
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "directConsumer2", queues = "${rabbit.service.direct.queue-south}")
    public void consumeDirectSouthMessage(Message message) {
        try {
            log.info("queue -> 'south', Received message: {}", message);
            byte[] messageBody = message.getBody();
            String actualMsgStr = new String(messageBody);
            log.info("Message -> {}", actualMsgStr);
           /* byte[] bytes = message.getBody();
            String bytesString = bytes.toString();
            log.info("Message received -> {} from queue -> 'north'", bytesString);*/
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "topicConsumer1", queues = "${rabbit.service.topic.queue-north}")
    public void consumeMsgNorthMessage(Message message) {
        try {
            log.info("queue -> 'north', Received message: {}", message);
            byte[] messageBody = message.getBody();
            String actualMsgStr = new String(messageBody);
            //log.info("Message -> {}", actualMsgStr);
           /* byte[] bytes = message.getBody();
            String bytesString = bytes.toString();
            log.info("Message received -> {} from queue -> 'north'", bytesString);*/
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "topicConsumer2", queues = "${rabbit.service.topic.queue-south}")
    public void consumeMsgSouth(Message message) {
        try {
            log.info("queue -> 'south', Received message: {}", message);
           /* byte[] bytes = message.getBody();
            String bytesString = bytes.toString();
            log.info("Message received -> {} from queue -> 'north'", bytesString);*/
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "fanoutConsumer1", queues = "${rabbit.service.fanout.queue-audit}")
    public void consumeMsgQueue1(@Payload String message) {
        try {
            log.info("queue -> 'queue1', Received message: {}", message);
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "fanoutConsumer2", queues = "${rabbit.service.fanout.queue-notification}")
    public void consumeMsgQueue2(@Payload String message) {
        try {
            log.info("queue -> 'queue2', Received message: {}", message);
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "fanoutConsumer3", queues = "${rabbit.service.fanout.queue-analytics}")
    public void consumeMsgQueue3(@Payload String message) {
        try {
            log.info("queue -> 'queue3', Received message: {}", message);
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "PriorityConsumer", queues = "${rabbit.service.headers.queue-priority}")
    public void priorityConsumer(@Payload String message) {
        try {
            log.info("queue -> {}, Received message: {}", rabbitMqProperties.getHeaders().getQueuePriority(), message);
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }

    @RabbitListener(id = "StandardConsumer", queues = "${rabbit.service.headers.queue-priority}")
    public void standardConsumer(@Payload String message) {
        try {
            log.info("queue -> {}, Received message: {}", rabbitMqProperties.getHeaders().getQueueStandard(), message);
        } catch (Exception e) {
            log.error("Failed to process message: {}. Error: {}", message, e.getMessage(), e);
        }
    }
}
