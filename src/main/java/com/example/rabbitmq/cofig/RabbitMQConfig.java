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

    /*Fan-out Exchange*/
    @Bean
    public Queue queue1() {
        return new Queue(rabbitMqProperties.getQueue1(), true);
    }

    @Bean
    public Queue queue2() {
        return new Queue(rabbitMqProperties.getQueue2(), true);
    }

    @Bean
    public Queue queue3() {
        return new Queue(rabbitMqProperties.getQueue3(), true);
    }
    @Bean
    public FanoutExchange fanoutExchange(){ return new FanoutExchange(rabbitMqProperties.getFanout_exchange()); }

    @Bean
    public Binding fanOutBinding() {
        return BindingBuilder.bind(queue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanOutBinding2(){
        return BindingBuilder.bind(queue2()).to(fanoutExchange());
    }
    @Bean
    public Binding fanOutBinding3(){
        return BindingBuilder.bind(queue3()).to(fanoutExchange());
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
