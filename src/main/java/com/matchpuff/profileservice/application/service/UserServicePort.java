package com.matchpuff.profileservice.application.service;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.application.dto.response.BatchProfileResponse;
import com.matchpuff.profileservice.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.StudentProfile;

public interface UserServicePort {
	UserResponse createStudentUser(StudentProfile user);
    UserResponse createAdminUser(Admin user);
    UserResponse createOrganizerUser(Organizer user);
    void deleteUser(UUID userId);
	UserResponse getUser(UUID userId);
    UserResponse getUserByEmail(String email);
	UserResponse updateUser(UUID userId, User user);
    void changePassword(UUID userId, String currentPassword, String newPassword);
    UserResponse addSchedule(UUID userId, Schedule schedule);
    UserResponse addTag(UUID userId, UUID tagId);
    UserResponse removeSchedule(UUID userId, Schedule schedule);
    UserResponse removeTag(UUID userId, UUID tagId);
    UserResponseProfilePhoto updateProfileImage(UUID userId, byte[] file, String contentType);
    UserResponse updateGeolocation(UUID userId, boolean geolocationEnabled);

    List<UserResponse> getAllUsers();
    List<UserResponse> getAllStudentProfiles();
    List<CategoryWithTagsResponse> getTagCatalog();


    List<UUID> getUserTags(UUID userId);
    List<String> getUserTagsNames(UUID userId);

    List<UUID> getUserFriends(UUID userId);
    UserResponse addFriend(UUID userId, UUID friendId);
    UserResponse removeFriend(UUID userId, UUID friendId);

    List<BatchProfileResponse> getUsersByIds(List<UUID> ids);

}

