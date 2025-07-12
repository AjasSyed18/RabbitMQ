package com.example.rabbitMQnKafka.producer;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RabbitmqProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitmqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void SendAndReceive(Message message) {
        log.info("Entered into SendAndReceive:::");
        rabbitTemplate.sendAndReceive(message);
    }
}
