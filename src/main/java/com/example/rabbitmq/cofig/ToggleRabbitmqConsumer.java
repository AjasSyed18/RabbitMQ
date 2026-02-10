package com.example.rabbitmq.cofig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ToggleRabbitmqConsumer {
    private static final Logger log = LoggerFactory.getLogger(ToggleRabbitmqConsumer.class);

    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Autowired
    public ToggleRabbitmqConsumer(RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry) {
        this.rabbitListenerEndpointRegistry = rabbitListenerEndpointRegistry;
    }

    @Value("${rabbitmq.consumer.topicA.enable}")
    private boolean rabbitConsumerTopicAEnable;

    @Value("${rabbitmq.consumer.topicB.enable}")
    private boolean rabbitConsumerTopicBEnable;

    @EventListener(ApplicationReadyEvent.class)
    public void toggleRabbitMQTopicAConsumerStartUp() {
        log.info("Entered into toggleRabbitMQTopicAConsumerStartUp :::");
        MessageListenerContainer consumerTopicA = Optional.ofNullable(
                        rabbitListenerEndpointRegistry.getListenerContainer("topicConsumer1"))
                .orElseThrow(() -> new IllegalStateException("Consumer container " + "consumerTopicA" + "not found"));
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
        final String CONSUMER_NAME = "topicConsumer2";

        try {
            MessageListenerContainer consumerTopicB = Optional.ofNullable(
                            rabbitListenerEndpointRegistry.getListenerContainer(CONSUMER_NAME))
                    .orElseThrow(() -> new IllegalStateException("Consumer Container " + CONSUMER_NAME + " not found"));

            if (rabbitConsumerTopicBEnable) {
                consumerTopicB.start();
                log.debug("Consumer Container {} started", CONSUMER_NAME);
            } else {
                consumerTopicB.stop();
                log.debug("Consumer Container {} stopped", CONSUMER_NAME);
            }
        } catch (IllegalStateException e) {
            log.error("Failed to toggle consumer {}: {}", CONSUMER_NAME, e.getMessage());
        }
    }

    public String toggleRabbitMQConsumerDynamic(boolean enable, String consumerContainerId) {
        log.info("Inside toggleRabbitMQConsumerDynamic::::");
        MessageListenerContainer listenerContainer = rabbitListenerEndpointRegistry.getListenerContainer(consumerContainerId);
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
