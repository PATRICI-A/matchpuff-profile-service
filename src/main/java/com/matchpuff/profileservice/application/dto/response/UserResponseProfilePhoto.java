package com.matchpuff.profileservice.application.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserResponseProfilePhoto {

    private UUID id;
    private String name;
    private String email;
    private String profileImageUrl;

}
