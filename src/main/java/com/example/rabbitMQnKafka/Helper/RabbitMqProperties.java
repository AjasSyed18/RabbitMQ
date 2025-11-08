package com.example.rabbitMQnKafka.Helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("rabbit.service")
@Configuration
@Getter
@Setter
public class RabbitMqProperties {

    public String queueA;
    public String queueB;
    private String exchange_name;
    private String routing_key;
}
