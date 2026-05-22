package com.matchpuff.profileservice.application.service;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileDto;

public interface InternalUserServicePort {

    UserAuthResponse getUser(UUID userId);
    UserAuthResponse getUserByEmail(String email);
    void verifyUser(UUID userId);
    void resetPassword(UUID userId, String newPassword);
    UserMatchProfileDto getProfileForMatching(UUID id);
    List<UserMatchProfileDto> getAllProfilesForMatching();
    List<UserMatchProfileDto> getAllProfilesForMatching(UUID requestingUserId);
}
