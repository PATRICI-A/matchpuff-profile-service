package com.matchpuff.profileservice.application.service;

import java.util.List;
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
    public void deleteUser(String userId) {
        userUseCase.deleteUser(userId);
    }

    @Override
    public UserResponse getUser(String userId) {
        User u = userUseCase.getUser(userId);
        return userMapper.toResponse(u);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User u = userUseCase.getUserByEmail(email);
        return userMapper.toResponse(u);
    }

    @Override
    public UserResponse updateUser(String userId, User user) {
        User updated = userUseCase.updateUser(userId, user);
        return userMapper.toResponse(updated);
    }

    @Override
    public void changePassword(String userId, String currentPassword, String newPassword) {
        userUseCase.changePassword(userId, currentPassword, newPassword);
    }

    @Override
    public UserResponse addSchedule(String userId, Schedule schedule) {
        User updated = userUseCase.addScheduleToStudent(userId, schedule);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse addTag(String userId, Tag tag) {
        User updated = userUseCase.addTagToStudent(userId, tag);
        return userMapper.toResponse(updated);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userUseCase.getAllUsers();
        return users.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponseProfilePhoto updateProfileImage(String userId, byte[] file, String contentType) {
        User updated = userUseCase.updateProfileImage(userId, file, contentType);
        return userMapper.toResponseProfilePhoto(updated);
    }

    @Override
    public List<UserResponse> getAllStudentProfiles() {
        List<StudentProfile> profiles = userUseCase.getAllStudentProfiles();
        return profiles.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse updateGeolocation(String userId, boolean geolocationEnabled) {
        User updated = userUseCase.updateGeolocation(userId, geolocationEnabled);
        return userMapper.toResponse(updated);
    }
}
