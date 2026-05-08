package com.matchpuff.profileservice.application.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthResponse {

    private UUID id;
    private String email;
    private String passwordHash;
    private boolean verified;
    private String userType;
}
