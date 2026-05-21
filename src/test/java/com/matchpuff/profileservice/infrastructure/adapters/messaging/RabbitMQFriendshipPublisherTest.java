package com.matchpuff.profileservice.infrastructure.adapters.messaging;

import com.matchpuff.profileservice.application.dto.event.FriendshipCreatedEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RabbitMQFriendshipPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private RabbitMQFriendshipPublisher publisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new RabbitMQFriendshipPublisher(rabbitTemplate);
    }

    @Test
    void publishFriendshipCreated_callsRabbitTemplateWithCorrectArgs() {

        publisher.publishFriendshipCreated(UUID.randomUUID(), UUID.randomUUID());

        verify(rabbitTemplate).convertAndSend(
                eq("friendship.exchange"),
                eq("friendship.created"),
                any(FriendshipCreatedEventDto.class)
        );
    }
}
