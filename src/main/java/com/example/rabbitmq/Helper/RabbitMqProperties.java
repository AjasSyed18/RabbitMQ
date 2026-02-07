package com.example.rabbitmq.Helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("rabbit.service")
@Configuration
@Getter
@Setter
public class RabbitMqProperties {

    public String queueA;
    public String queueB;
    public String queue1;
    public String queue2;
    public String queue3;
    private String exchange_name;
    private String routing_key;
    private String fanout_exchange;
}
