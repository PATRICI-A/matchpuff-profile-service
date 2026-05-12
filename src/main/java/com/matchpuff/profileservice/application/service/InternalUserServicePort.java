package com.matchpuff.profileservice.application.service;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse;

public interface InternalUserServicePort {

    UserAuthResponse getUser(UUID userId);
    UserAuthResponse getUserByEmail(String email);
    void verifyUser(UUID userId);
    UserMatchProfileResponse getProfileForMatching(UUID id);
    List<UserMatchProfileResponse> getAllProfilesForMatching();
}
