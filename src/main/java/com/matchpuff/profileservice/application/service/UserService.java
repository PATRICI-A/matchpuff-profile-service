package com.matchpuff.profileservice.application.service;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.application.dto.response.BatchProfileResponse;
import com.matchpuff.profileservice.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.profileservice.application.dto.response.TagSummaryResponse;
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

    @Override
    public List<UUID> getUserTags(UUID userId) {
        return userUseCase.getUserTags(userId);
    }

    @Override
    public List<String> getUserTagsNames(UUID userId) {
        return userUseCase.getUserTagsNames(userId);
    }

    @Override
    public List<UUID> getUserFriends(UUID userId) {
        return userUseCase.getUserFriends(userId);
    }

    @Override
    public UserResponse addFriend(UUID userId, UUID friendId) {
        User updated = userUseCase.addFriendToStudent(userId, friendId);
        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse removeFriend(UUID userId, UUID friendId) {
        User updated = userUseCase.removeFriendFromStudent(userId, friendId);
        return userMapper.toResponse(updated);
    }

    @Override
    public List<BatchProfileResponse> getUsersByIds(List<UUID> ids) {
        return userUseCase.getUsersByIds(ids).stream()
                .map(user -> {
                    String biography = user instanceof StudentProfile s ? s.getBiography() : null;
                    String photoUrl = user instanceof StudentProfile s2 ? s2.getPhotoUrl() : null;
                    return new BatchProfileResponse(user.getId(), user.getName(), user.getEmail(), biography, photoUrl);
                })
                .toList();
    }

    @Override
    public List<CategoryWithTagsResponse> getTagCatalog() {
        return userUseCase.getTagCatalog().stream()
                .map(cat -> new CategoryWithTagsResponse(
                        cat.getId(),
                        cat.getName(),
                        cat.getTags().stream()
                                .map(t -> new TagSummaryResponse(t.getId(), t.getName(), t.getCategoryId()))
                                .toList()
                ))
                .toList();
    }
}
