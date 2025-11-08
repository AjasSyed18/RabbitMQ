package com.example.rabbitMQnKafka.controller;

import com.example.rabbitMQnKafka.cofig.ToggleRabbitmqConsumer;
import com.example.rabbitMQnKafka.producer.RabbitmqProducer;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class RabbitmqController {

    private final MessageConverter messageConverter = new SimpleMessageConverter();

    @Autowired
    private RabbitmqProducer rabbitmqProducer;

    @Autowired
    private ToggleRabbitmqConsumer toggleRabbitmqConsumer;

    @PostMapping(value = "/publishMsgDemo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publish(@RequestParam String strMessage) {
        log.info("Inside demo ::::: {}", strMessage);
        Message message = messageConverter.toMessage(strMessage, new MessageProperties());
        rabbitmqProducer.SendAndReceive(message);
        JSONObject respJson = new JSONObject();
        respJson.put("code", "200");
        respJson.put("message", "message published!");
        return ResponseEntity.ok().body(respJson.toString());
    }

    @PostMapping(value = "/publishMsg/{msg}")
    public void publishMsg(@PathVariable("msg") String msg) {
        Message message = messageConverter.toMessage(msg, new MessageProperties());
        rabbitmqProducer.sendMessage(message);
    }

    @PostMapping(path = "/rabbitmq/consumer/enable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> enableRabbitMQConsumer(@RequestParam boolean enableConsumer,
                                                    @RequestParam String consumerContainerId) {
        log.info("Entered into enableRabbitMQConsumer:::: consumerContainerId -> {}, enableConsumer -> {}", consumerContainerId, enableConsumer);
        String response = toggleRabbitmqConsumer.toggleRabbitMQConsumerDynamic(enableConsumer, consumerContainerId);
        log.info("response -> {}", response);
        return ResponseEntity.ok(new JSONObject().put("code", "200").put("message", response).toString());
    }
}