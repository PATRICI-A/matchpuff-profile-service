package com.matchpuff.profileservice.infrastructure.adapters.messaging;

import com.matchpuff.profileservice.application.dto.event.FriendshipCreatedNotificationDto;
import com.matchpuff.profileservice.domain.ports.out.FriendshipEventPublisherPort;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQFriendshipPublisher implements FriendshipEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.friendship}")
    private String friendshipExchange;

    @Value("${rabbitmq.routing-key.friendship-created}")
    private String friendshipCreatedRoutingKey;

    @Override
    public void publishFriendshipCreated(FriendshipCreatedNotificationDto event) {
        rabbitTemplate.convertAndSend(friendshipExchange, friendshipCreatedRoutingKey, event);
    }
}
