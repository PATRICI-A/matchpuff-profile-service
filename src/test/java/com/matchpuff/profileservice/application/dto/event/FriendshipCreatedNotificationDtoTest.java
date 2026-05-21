package com.matchpuff.profileservice.application.dto.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipCreatedNotificationDtoTest {

    @Test
    void builderAndGetters_workCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 2, 3, 4, 5);

        FriendshipCreatedEventDto dto = FriendshipCreatedEventDto.builder()
                .userId1("userA")
                .userId2("userB")
                .createdAt(createdAt)
                .build();

        assertEquals("userA", dto.getUserId1());
        assertEquals("userB", dto.getUserId2());
        assertEquals(createdAt, dto.getCreatedAt());
        assertNotNull(dto.toString());
    }

    @Test
    void noArgsConstructorAndSetters_workCorrectly() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 6, 7, 8, 9, 10);

        FriendshipCreatedEventDto dto = new FriendshipCreatedEventDto();
        dto.setUserId1("u1");
        dto.setUserId2("u2");
        dto.setCreatedAt(createdAt);

        assertEquals("u1", dto.getUserId1());
        assertEquals("u2", dto.getUserId2());
        assertEquals(createdAt, dto.getCreatedAt());
    }

    @Test
    void equalsAndHashCode_considerAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 12, 31, 23, 59, 59);

        FriendshipCreatedEventDto a = new FriendshipCreatedEventDto("x", "y", createdAt);
        FriendshipCreatedEventDto b = new FriendshipCreatedEventDto("x", "y", createdAt);
        FriendshipCreatedEventDto c = new FriendshipCreatedEventDto("x", "z", createdAt);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
    }

    @Test
    void toString_containsFieldValues() {
        FriendshipCreatedEventDto dto = FriendshipCreatedEventDto.builder()
                .userId1("alpha")
                .userId2("beta")
                .createdAt(LocalDateTime.of(2022, 2, 2, 2, 2))
                .build();

        String s = dto.toString();
        assertTrue(s.contains("alpha"));
        assertTrue(s.contains("beta"));
        assertTrue(s.contains("2022-02-02"));
    }
}
