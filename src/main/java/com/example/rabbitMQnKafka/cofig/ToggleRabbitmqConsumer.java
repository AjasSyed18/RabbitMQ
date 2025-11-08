package com.example.rabbitMQnKafka.cofig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ToggleRabbitmqConsumer {
    private static final Logger log = LoggerFactory.getLogger(ToggleRabbitmqConsumer.class);
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Value("${rabbitmq.consumer.topicA.enable}")
    private boolean rabbitConsumerTopicAEnable;

    @Value("${rabbitmq.consumer.topicB.enable}")
    private boolean rabbitConsumerTopicBEnable;

    @EventListener(ApplicationReadyEvent.class)
    public void toggleRabbitMQTopicAConsumerStartUp() {
        log.info("Entered into toggleRabbitMQTopicAConsumerStartUp :::");
        MessageListenerContainer consumerTopicA = rabbitListenerEndpointRegistry.getListenerContainer("consumerTopicA");
        if (consumerTopicA != null) {
            log.info("Consumer Container consumerTopicA found :::");
            if (rabbitConsumerTopicAEnable) {
                consumerTopicA.start();
                log.info("Consumer Container consumerTopicA Started :::");
            } else {
                consumerTopicA.stop();
                log.info("Consumer Container consumerTopicA Stopped :::");
            }
        } else {
            log.info("Consumer Container consumerTopicA not found :::");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void toggleRabbitMQTopicBConsumerStartUp() {
        MessageListenerContainer consumerTopicB = rabbitListenerEndpointRegistry.getListenerContainer("consumerTopicB");
        if (consumerTopicB != null) {
            log.info("Consumer Container consumerTopicB found :::");
            if (rabbitConsumerTopicBEnable) {
                consumerTopicB.start();
                log.info("Consumer Container consumerTopicB Started :::");
            } else {
                consumerTopicB.stop();
                log.info("Consumer Container consumerTopicB Stop :::");
            }
        } else {
            log.info("Consumer Container consumerTopicB not found :::");
        }
    }

    public String toggleRabbitMQConsumerDynamic(boolean enable, String consumerContainerId) {
        log.info("Inside toggleRabbitMQConsumerDynamic::::");
        MessageListenerContainer listenerContainer = rabbitListenerEndpointRegistry.getListenerContainer(consumerContainerId);
        boolean running = listenerContainer.isRunning();
        if (enable) {
            listenerContainer.start();
            log.info("Started Consumer -> {}", consumerContainerId);
            return String.format("Consumer Container %s Started", consumerContainerId);
        } else {
            listenerContainer.stop();
            log.info("Stopped Consumer -> {}", consumerContainerId);
            return String.format("Consumer Container %s Stopped", consumerContainerId);
        }
    }
}
