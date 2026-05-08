package com.matchpuff.profileservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.application.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class InternalUserService implements InternalUserServicePort {
    private final UserUseCasePort userUseCase;
    private final UserMapper userMapper;

    @Override
    public UserAuthResponse getUser(String userId) {
        return userMapper.toAuthResponse(userUseCase.getUser(userId));
    }

    @Override
    public UserAuthResponse getUserByEmail(String email) {
        return userMapper.toAuthResponse(userUseCase.getUserByEmail(email));
    }

    @Override
    public void verifyUser(String userId) {
        userUseCase.verifyUser(userId);
    }

}
