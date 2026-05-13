package com.matchpuff.profileservice.application.service;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.*;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {
    private final UserUseCasePort userUseCase;
    private final UserMapper userMapper;

    @Override
    public UserResponse createStudentUser(StudentProfile user) {
        User created = userUseCase.createStudentUser(user);
        return userMapper.toResponse(created);
    }

    @Override
    public UserResponse createAdminUser(Admin user) {
        User created = userUseCase.createAdminUser(user);
        return userMapper.toResponse(created);
    }

    @Override
    public UserResponse createOrganizerUser(Organizer user) {
        User created = userUseCase.createOrganizerUser(user);
        return userMapper.toResponse(created);
    }

    @Override
    public void deleteUser(UUID userId) {
        userUseCase.deleteUser(userId);
    }

    @Override
    public UserResponse getUser(UUID userId) {
        User u = userUseCase.getUser(userId);
        return userMapper.toResponse(u);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User u = userUseCase.getUserByEmail(email);
        return userMapper.toResponse(u);
    }

    @Override
    public UserResponse updateUser(UUID userId, User user) {
        User updated = userUseCase.updateUser(userId, user);
        return userMapper.toResponse(updated);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        userUseCase.changePassword(userId, currentPassword, newPassword);
    }

    @Override
    public UserResponse addSchedule(UUID userId, Schedule schedule) {
        User updated = userUseCase.addScheduleToStudent(userId, schedule);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse removeSchedule(UUID userId, Schedule schedule) {
        User updated = userUseCase.removeScheduleFromStudent(userId, schedule);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse removeTag(UUID userId, UUID tagId) {
        User updated = userUseCase.removeTagFromStudent(userId, tagId);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse addTag(UUID userId, UUID tagId) {
        User updated = userUseCase.addTagToStudent(userId, tagId);
        return userMapper.toResponse(updated);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userUseCase.getAllUsers();
        return users.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponseProfilePhoto updateProfileImage(UUID userId, byte[] file, String contentType) {
        User updated = userUseCase.updateProfileImage(userId, file, contentType);
        return userMapper.toResponseProfilePhoto(updated);
    }

    @Override
    public List<UserResponse> getAllStudentProfiles() {
        List<StudentProfile> profiles = userUseCase.getAllStudentProfiles();
        return profiles.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse updateGeolocation(UUID userId, boolean geolocationEnabled) {
        User updated = userUseCase.updateGeolocation(userId, geolocationEnabled);
        return userMapper.toResponse(updated);
    }
}
