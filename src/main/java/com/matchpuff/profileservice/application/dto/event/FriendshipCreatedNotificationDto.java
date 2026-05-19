package com.matchpuff.profileservice.application.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipCreatedNotificationDto {

    private String userId1;
    private String userId2;
    private LocalDateTime createdAt;
}
