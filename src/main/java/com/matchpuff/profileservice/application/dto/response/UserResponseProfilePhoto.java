package com.matchpuff.profileservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserResponseProfilePhoto {

    private String id;
    private String name;
    private String email;
    private String profileImageUrl;

}
