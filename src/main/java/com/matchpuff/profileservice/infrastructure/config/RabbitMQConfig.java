package com.matchpuff.profileservice.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.friendship}")
    private String friendshipExchange;

    @Value("${rabbitmq.queue.friendship-created}")
    private String friendshipCreatedQueue;

    @Value("${rabbitmq.routing-key.friendship-created}")
    private String friendshipCreatedRoutingKey;

    @Bean
    public TopicExchange friendshipExchange() {
        return new TopicExchange(friendshipExchange);
    }

    @Bean
    public Queue friendshipCreatedQueue() {
        return new Queue(friendshipCreatedQueue, true);
    }

    @Bean
    public Binding friendshipCreatedBinding(Queue friendshipCreatedQueue, TopicExchange friendshipExchange) {
        return BindingBuilder.bind(friendshipCreatedQueue)
                .to(friendshipExchange)
                .with(friendshipCreatedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
