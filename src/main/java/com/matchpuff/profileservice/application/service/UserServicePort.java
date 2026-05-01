package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.request.UpdateUserRequest;
import com.matchpuff.profileservice.application.dto.request.UserRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;

public interface UserServicePort {

    UserResponse createUser(UserRequest request);

    UserResponse getUser(String userId);

    UserResponse updateUser(String userId, UserRequest request);

}
