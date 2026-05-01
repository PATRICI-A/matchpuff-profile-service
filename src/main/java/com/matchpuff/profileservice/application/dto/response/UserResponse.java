package com.matchpuff.profileservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private String userType;
}
