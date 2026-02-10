package com.example.rabbitmq.cofig;

import com.example.rabbitmq.Helper.RabbitMqProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    public String host;

    @Value("${spring.rabbitmq.port}")
    public int port;

    @Value("${spring.rabbitmq.username}")
    public String username;

    @Value("${spring.rabbitmq.password}")
    public String password;

    @Value("${spring.rabbitmq.virtual-host}")
    public String virtualHost;

    @Value("${spring.rabbitmq.connectionTimeout}")
    public int connectionTimeout;

    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    public RabbitMQConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

/* =========================
   DIRECT EXCHANGE
   ========================= */

    @Bean
    public Queue directQueueNorth() {
        return new Queue(rabbitMqProperties.getDirect().getQueueNorth(), false, false, true);
    }

    @Bean
    public Queue directQueueSouth() {
        return new Queue(rabbitMqProperties.getDirect().getQueueSouth(), false, false, true);
    }

    @Bean
    public DirectExchange directExchangeNorth() {
        return new DirectExchange(rabbitMqProperties.getDirect().getExchangeNorth());
    }

    @Bean
    public DirectExchange directExchangeSouth() {
        return new DirectExchange(rabbitMqProperties.getDirect().getExchangeSouth());
    }

    @Bean
    public Binding directNorthBinding() {
        return BindingBuilder
                .bind(directQueueNorth())
                .to(directExchangeNorth())
                .with(rabbitMqProperties.getDirect().getExchangeKeyNorth());
    }

    @Bean
    public Binding directSouthBinding() {
        return BindingBuilder
                .bind(directQueueSouth())
                .to(directExchangeSouth())
                .with(rabbitMqProperties.getDirect().getExchangeKeySouth());
    }

    /* =========================
   TOPIC EXCHANGE
   ========================= */

    @Bean
    public Queue topicQueueNorth() {
        return new Queue(rabbitMqProperties.getTopic().getQueueNorth(), false, false, true);
    }

    @Bean
    public Queue topicQueueSouth() {
        return new Queue(rabbitMqProperties.getTopic().getQueueSouth(), false, false, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(rabbitMqProperties.getTopic().getExchange());
    }

    @Bean
    public Binding topicNorthBinding() {
        return BindingBuilder
                .bind(topicQueueNorth())
                .to(topicExchange())
                .with("topic.exchange.*");
    }

    @Bean
    public Binding topicSouthBinding() {
        return BindingBuilder
                .bind(topicQueueSouth())
                .to(topicExchange())
                .with("topic.exchange.*");
    }

    /* =========================
   FANOUT EXCHANGE
   ========================= */

    @Bean
    public Queue fanoutAuditQueue() {
        return new Queue(rabbitMqProperties.getFanout().getQueueAudit(), false, false, true);
    }

    @Bean
    public Queue fanoutNotificationQueue() {
        return new Queue(rabbitMqProperties.getFanout().getQueueNotification(), false, false, true);
    }

    @Bean
    public Queue fanoutAnalyticsQueue() {
        return new Queue(rabbitMqProperties.getFanout().getQueueAnalytics(), false, false, true);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(rabbitMqProperties.getFanout().getExchange());
    }

    @Bean
    public Binding fanoutAuditBinding() {
        return BindingBuilder.bind(fanoutAuditQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutNotificationBinding() {
        return BindingBuilder.bind(fanoutNotificationQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutAnalyticsBinding() {
        return BindingBuilder.bind(fanoutAnalyticsQueue()).to(fanoutExchange());
    }

    /* =========================
   HEADER EXCHANGE
   ========================= */
    @Bean
    public Queue queuePriority(){
        return new Queue("headers.priority.queue", false, false, true);
    }

    @Bean
    public Queue queueStandard(){
        return new Queue("headers.standard.queue", false, false, true);
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange("headers.exchange");
    }

    @Bean
    public Binding bindingPriority() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "priority");
        headers.put("region", "north");

        return BindingBuilder
                .bind(queuePriority())
                .to(headersExchange())
                .whereAll(headers)
                .match();
    }

    @Bean
    public Binding bindingStandard() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "standard");
        headers.put("region", "south");

        return BindingBuilder
                .bind(queueStandard())
                .to(headersExchange())
                .whereAll(headers)   // AND logic (recommended)
                .match();
    }
    /*----------------------------------------------------------------------------------------------------*/
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(host);
        cachingConnectionFactory.setPort(port);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        cachingConnectionFactory.setConnectionTimeout(connectionTimeout);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        return container;
    }


}
