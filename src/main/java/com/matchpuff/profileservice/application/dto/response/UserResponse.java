package com.matchpuff.profileservice.application.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
public class UserResponse {

    protected UUID id;
    protected String name;
    protected String email;
    protected LocalDateTime createdAt;
    protected String gender;
    protected String userType;

}
