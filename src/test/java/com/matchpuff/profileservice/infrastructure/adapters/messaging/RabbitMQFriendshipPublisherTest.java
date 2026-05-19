package com.matchpuff.profileservice.infrastructure.adapters.messaging;

import com.matchpuff.profileservice.application.dto.event.FriendshipCreatedNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RabbitMQFriendshipPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private RabbitMQFriendshipPublisher publisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new RabbitMQFriendshipPublisher(rabbitTemplate);

        // set the exchange and routing key as if @Value injected them
        ReflectionTestUtils.setField(publisher, "friendshipExchange", "exchange.test");
        ReflectionTestUtils.setField(publisher, "friendshipCreatedRoutingKey", "routing.key.test");
    }

    @Test
    void publishFriendshipCreated_callsRabbitTemplateWithCorrectArgs() {
        FriendshipCreatedNotificationDto event = FriendshipCreatedNotificationDto.builder()
                .userId1("x")
                .userId2("y")
                .createdAt(LocalDateTime.now())
                .build();

        publisher.publishFriendshipCreated(event);

        verify(rabbitTemplate).convertAndSend("exchange.test", "routing.key.test", event);
    }
}
