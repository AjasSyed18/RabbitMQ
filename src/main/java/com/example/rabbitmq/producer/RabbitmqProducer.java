package com.example.rabbitmq.producer;

import com.example.rabbitmq.Helper.RabbitMqProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

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

    @Autowired
    private RabbitMqProperties rabbitMqProperties;
/*
    public void sendMessage(Message msg) {
        rabbitTemplate.send(
                rabbitMqProperties.getExchange_name(),   // exchange
                rabbitMqProperties.getRouting_key(),     // routing key
                msg                                      // message
        );
    }*/

    public void publishTopicExchangeMessage(Message msg) {
        log.info("Published msg: {} \nto exchange: {}", msg, rabbitMqProperties.getTopic().getExchange());
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getTopic().getExchange(),   // order.topic.exchange
                "topic.exchange.anything",// routing key
                msg
        );
    }

    public void publishFanOutMessage(Message msg) {
        log.info("Published msg: {} \nto exchange: {}", msg, rabbitMqProperties.getFanout().getExchange());
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getFanout().getExchange(), // fanout exchange
                "",                                              // routing key ignored
                msg                                              // message
        );
    }

    public void publishDirectNorthMessage(Message msg) {
        log.info("Published msg: {} \nto exchange: {}", msg, rabbitMqProperties.getDirect().getExchangeKeyNorth());
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getDirect().getExchangeNorth(),
                rabbitMqProperties.getDirect().getExchangeKeyNorth(),
                msg
        );
    }

    public void publishDirectSouthMessage(Message msg) {
        log.info("Published msg: {} \nto exchange: {}", msg, rabbitMqProperties.getDirect().getExchangeKeySouth());
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.getDirect().getExchangeSouth(),
                rabbitMqProperties.getDirect().getExchangeKeySouth(),
                msg
        );
    }

    public void publishHeadersPriorityMsg(String strMsg){
        log.info("Published msg: {} \nto exchange: {}", strMsg, rabbitMqProperties.getHeaders().getExchange());
        MessageProperties properties = new MessageProperties();
        properties.setHeader("type", "priority");
        properties.setHeader("region", "north");
        Message message = new Message(strMsg.getBytes(), properties);
        rabbitTemplate.convertAndSend(
                "headers.exchange",
                "", //routing key ignored
                message);

    }

    public void publishHeadersStandardMsg(String strMsg){
        log.info("Published msg: {} \nto exchange: {}", strMsg, rabbitMqProperties.getHeaders().getExchange());
        MessageProperties properties = new MessageProperties();
        properties.setHeader("type", "standard");
        properties.setHeader("region", "south");
        Message message = new Message(strMsg.getBytes(), properties);
        rabbitTemplate.convertAndSend(
                "headers.exchange",
                "", //routing key ignored
                message);

    }
}
