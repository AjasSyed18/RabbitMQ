package com.example.rabbitmq.cofig;

import com.example.rabbitmq.Helper.RabbitMqProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /*@Value("${rabbit.service.queueA}")
    public String queueA;

    @Value("${rabbit.service.queueB}")
    public String queueB;

    @Value("${rabbit.service.exchange_name}")
    public String exchange;

    @Value("${rabbit.service.routing_key}")
    public String routingKey;*/

    private RabbitMqProperties rabbitMqProperties;
    @Autowired
    public RabbitMQConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public Queue queueA() {
        return new Queue(rabbitMqProperties.getQueueA(), true);
    }

    @Bean
    public Queue queueB(){
        return new Queue(rabbitMqProperties.getQueueB(), true);
    }

    @Bean
    public TopicExchange exchangeA() {
        return new TopicExchange(rabbitMqProperties.getExchange_name());
    }

    @Bean
    public TopicExchange exchangeB(){
        return new TopicExchange(rabbitMqProperties.getExchange_name());
    }

    @Bean
    public Binding bindingA(){
        return BindingBuilder.bind(queueB()).to(exchangeB()).with(rabbitMqProperties.getRouting_key());
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueA()).to(exchangeA()).with(rabbitMqProperties.getRouting_key());
    }

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
