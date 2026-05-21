package com.matchpuff.profileservice.application.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipCreatedEventDto {

    private UUID userId1;
    private UUID userId2;
    private LocalDateTime createdAt;
}
