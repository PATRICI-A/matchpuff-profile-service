package com.matchpuff.profileservice.application.service;

import java.util.List;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {
    private final UserUseCasePort userUseCase;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(StudentProfile user) {
        User created = userUseCase.createStudentUser(user);
        return userMapper.toResponse(created);
    }

    @Override
    public UserResponseInfo getUser(String userId) {
        User u = userUseCase.getUser(userId);
        return userMapper.toResponseInfo(u);
    }

    @Override
    public UserResponseInfo updateUser(String userId, StudentProfile user) {
        User updated = userUseCase.updateStudentUser(userId, user);
        return userMapper.toResponseInfo(updated);
    }

    @Override
    public UserResponseInfo addSchedule(String userId, Schedule schedule) {
        User updated = userUseCase.addScheduleToStudent(userId, schedule);
        return userMapper.toResponseInfo(updated);
    }

    @Override
    public UserResponseInfo addTag(String userId, Tag tag) {
        User updated = userUseCase.addTagToStudent(userId, tag);
        return userMapper.toResponseInfo(updated);
    }

    @Override
    public List<UserResponseInfo> getAllUsers() {
        List<User> users = userUseCase.getAllUsers();
        return users.stream().map(userMapper::toResponseInfo).toList();
    }
}
