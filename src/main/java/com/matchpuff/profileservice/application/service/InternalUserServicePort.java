package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;

public interface InternalUserServicePort {

    UserAuthResponse getUser(String userId);
    UserAuthResponse getUserByEmail(String email);
    void verifyUser(String userId);
}
