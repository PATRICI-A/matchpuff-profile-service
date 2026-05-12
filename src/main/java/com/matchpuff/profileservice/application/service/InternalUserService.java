package com.matchpuff.profileservice.application.service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.application.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class InternalUserService implements InternalUserServicePort {  //IMPORTANTE el caso de uso lo deberia manejar
    private final UserUseCasePort userUseCase;
    private final UserMapper userMapper;

    @Override
    public UserAuthResponse getUser(UUID userId) {
        return userMapper.toAuthResponse(userUseCase.getUser(userId));
    }

    @Override
    public UserAuthResponse getUserByEmail(String email) {
        return userMapper.toAuthResponse(userUseCase.getUserByEmail(email));
    }

    @Override
    public void verifyUser(UUID userId) {
        userUseCase.verifyUser(userId);
    }

    @Override
    public UserMatchProfileResponse getProfileForMatching(UUID id) {
        return userMapper.toUserMatchProfileResponseFromUser(userUseCase.getUser(id));
    }

    @Override
    public List<UserMatchProfileResponse> getAllProfilesForMatching() {
        return userUseCase.getAllUsers().stream()
                .map(userMapper::toUserMatchProfileResponseFromUser)
                .toList();
    }

}
