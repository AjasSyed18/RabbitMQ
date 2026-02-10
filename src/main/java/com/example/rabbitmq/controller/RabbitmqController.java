package com.example.rabbitmq.controller;

import com.example.rabbitmq.cofig.ToggleRabbitmqConsumer;
import com.example.rabbitmq.producer.RabbitmqProducer;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RestController
public class RabbitmqController {

    private final MessageConverter messageConverter = new SimpleMessageConverter();

    @Autowired
    private RabbitmqProducer rabbitmqProducer;

    @Autowired
    private ToggleRabbitmqConsumer toggleRabbitmqConsumer;

/*

    @PostMapping(value = "/publishMsgDemo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publish(@RequestParam("key") String strMessage) {
        log.info("Inside demo ::::: {}", strMessage);
        Message message = messageConverter.toMessage(strMessage, new MessageProperties());
        rabbitmqProducer.SendAndReceive(message);
        JSONObject respJson = new JSONObject();
        respJson.put("code", "200");
        respJson.put("message", "message published!");
        return ResponseEntity.ok().body(respJson.toString());
    }
*/

   /* @PostMapping(value = "/publishMsg/{msg}")
    public void publishMsg(@PathVariable("msg") String msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("traceId", UUID.randomUUID().toString());
        Message message = messageConverter.toMessage(msg, messageProperties);
        rabbitmqProducer.sendMessage(message);
    }*/

   @PostMapping(value = "/publishMsgDirectSouth/{msg}")
   public void publishMsgDirectSouth(@PathVariable("msg") String msg) {
       MessageProperties messageProperties = new MessageProperties();
       messageProperties.setHeader("traceId", UUID.randomUUID().toString());
       messageProperties.setContentType("application/json");
       messageProperties.setHeader("exchange-type", "Topic");
       Message message = messageConverter.toMessage(msg, messageProperties);
       rabbitmqProducer.publishDirectSouthMessage(message);
   }

    @PostMapping(value = "/publishMsgFanout/{msg}")
    public void publishMsgFanout(@PathVariable("msg") String msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("traceId", UUID.randomUUID().toString());
        messageProperties.setContentType("application/json");
        messageProperties.setHeader("exchange-type", "Fan-out");
        Message message = messageConverter.toMessage(msg, messageProperties);
        rabbitmqProducer.publishFanOutMessage(message);
    }

    @PostMapping(value = "/publishMsgTopic/{msg}")
    public void publishMsgTopic(@PathVariable("msg") String msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("traceId", UUID.randomUUID().toString());
        messageProperties.setContentType("application/json");
        messageProperties.setHeader("exchange-type", "Topic");
        Message message = messageConverter.toMessage(msg, messageProperties);
        rabbitmqProducer.publishTopicExchangeMessage(message);
    }

    @PostMapping(value = "/publishMsgDirectNorth/{msg}")
    public void publishMsgDirectNorth(@PathVariable("msg") String msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("traceId", UUID.randomUUID().toString());
        messageProperties.setContentType("application/json");
        messageProperties.setHeader("exchange-type", "Topic");
        Message message = messageConverter.toMessage(msg, messageProperties);
        rabbitmqProducer.publishDirectNorthMessage(message);
    }

    @PostMapping(value = "/publishHeadersPriority/{msg}")
    public void publishHeadersPriority(@PathVariable("msg") String msg) {
        rabbitmqProducer.publishHeadersPriorityMsg(msg);
    }

    @PostMapping(value = "/publishHeadersStandard/{msg}")
    public void publishHeadersStandard(@PathVariable("msg") String msg) {
        rabbitmqProducer.publishHeadersStandardMsg(msg);
    }

    @PostMapping(path = "/rabbitmq/consumer/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> enableRabbitMQConsumer(@RequestParam boolean enableConsumer,
                                                    @RequestParam String consumerContainerId) {
        log.info("Entered into enableRabbitMQConsumer:::: consumerContainerId -> {}, enableConsumer -> {}", consumerContainerId, enableConsumer);
        String response = toggleRabbitmqConsumer.toggleRabbitMQConsumerDynamic(enableConsumer, consumerContainerId);
        log.info("response -> {}", response);
        return ResponseEntity.ok(new JSONObject().put("code", "200").put("message", response).toString());
    }

   /* @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        publishMsg("startUpMessage");
        *//*rabbitTemplate.convertAndSend("exchangeName", "north", payload, message -> {
            message.getMessageProperties().setHeader("source", "order-service");
            message.getMessageProperties().setHeader("region", "north");
            message.getMessageProperties().setHeader("traceId", UUID.randomUUID().toString());
            return message;
        });*//*
    }*/
}
