package com.example.rabbitmq.Helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "rabbit.service")
@Getter
@Setter
public class RabbitMqProperties {

    private Direct direct = new Direct();
    private Topic topic = new Topic();
    private Fanout fanout = new Fanout();

    // =========================
    // DIRECT
    // =========================
    @Getter
    @Setter
    public static class Direct {
        private String exchangeNorth;
        private String exchangeSouth;
        private String exchangeKeyNorth;
        private String exchangeKeySouth;
        private String queueNorth;
        private String queueSouth;
    }

    // =========================
    // TOPIC
    // =========================
    @Getter
    @Setter
    public static class Topic {
        private String exchange;
        private String exchangeKey;
        private String routingKey;
        private String queueNorth;
        private String queueSouth;
    }

    // =========================
    // FANOUT
    // =========================
    @Getter
    @Setter
    public static class Fanout {
        private String exchange;
        private String queueAudit;
        private String queueNotification;
        private String queueAnalytics;
    }
}