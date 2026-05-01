package com.matchpuff.profileservice.application.service;

import java.util.List;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;

public interface UserServicePort {
	UserResponse createUser(StudentProfile user);
	UserResponseInfo getUser(String userId);
	UserResponseInfo updateUser(String userId, StudentProfile user);
    UserResponseInfo addSchedule(String userId, Schedule schedule);
    UserResponseInfo addTag(String userId, Tag tag);
    List<UserResponseInfo> getAllUsers();
}

