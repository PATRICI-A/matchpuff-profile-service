package com.matchpuff.profileservice.infrastructure.adapters.messaging;

import com.matchpuff.profileservice.application.dto.event.FriendshipCreatedEventDto;
import com.matchpuff.profileservice.domain.ports.out.FriendshipEventPublisherPort;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQFriendshipPublisher implements FriendshipEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishFriendshipCreated(UUID userId1, UUID userId2) {
        FriendshipCreatedEventDto event = FriendshipCreatedEventDto.builder()
                .userId1(userId1)
                .userId2(userId2)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        rabbitTemplate.convertAndSend("friendship.exchange","friendship.created", event);
    }
}
