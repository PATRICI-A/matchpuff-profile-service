package com.matchpuff.profileservice.application.dto.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipCreatedNotificationDtoTest {

    @Test
    void builderAndGetters_workCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 2, 3, 4, 5);

        FriendshipCreatedEventDto dto = FriendshipCreatedEventDto.builder()
                .userId1(UUID.randomUUID())
                .userId2(UUID.randomUUID())
                .createdAt(createdAt)
                .build();

        assertEquals(dto.getUserId1(), dto.getUserId1());
        assertEquals(dto.getUserId2(), dto.getUserId2());
        assertEquals(createdAt, dto.getCreatedAt());
        assertNotNull(dto.toString());
    }

    @Test
    void noArgsConstructorAndSetters_workCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 6, 7, 8, 9, 10);

        FriendshipCreatedEventDto dto = new FriendshipCreatedEventDto();
        dto.setUserId1(UUID.randomUUID());
        dto.setUserId2(UUID.randomUUID());
        dto.setCreatedAt(createdAt);

        assertEquals(dto.getUserId1(), dto.getUserId1());
        assertEquals(dto.getUserId2(), dto.getUserId2());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void toString_containsFieldValues() {
        FriendshipCreatedEventDto dto = FriendshipCreatedEventDto.builder()
                .userId1(UUID.randomUUID())
                .userId2(UUID.randomUUID())
                .createdAt(LocalDateTime.of(2022, 2, 2, 2, 2))
                .build();

        String s = dto.toString();
        assertTrue(s.contains(dto.getUserId1().toString()));
        assertTrue(s.contains(dto.getUserId2().toString()));
        assertTrue(s.contains("2022-02-02"));
    }
}
