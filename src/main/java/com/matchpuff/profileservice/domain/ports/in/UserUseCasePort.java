package com.matchpuff.profileservice.domain.ports.in;

import com.matchpuff.profileservice.domain.model.*;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.domain.model.CategoryWithTags;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;

public interface UserUseCasePort {
    User createStudentUser(StudentProfile student);
    User createAdminUser(Admin admin);
    User createOrganizerUser(Organizer organizer);
    void deleteUser(UUID userId);
    User getUser(UUID userId);
    User getUserByEmail(String email);
    User updateUser(UUID userId, User user);
    void changePassword(UUID userId, String currentPassword, String newPassword);
    void verifyUser(UUID userId);
    User addScheduleToStudent(UUID userId, Schedule schedule);
    User addTagToStudent(UUID userId, UUID tagId);
    User removeScheduleFromStudent(UUID userId, Schedule schedule);
    User removeTagFromStudent(UUID userId, UUID tagId);
    User updateProfileImage(UUID userId, byte[] file, String contentType);
    User updateGeolocation(UUID userId, boolean geolocationEnabled);
    List<User> getAllUsers();
    List<StudentProfile> getAllStudentProfiles();
    List<CategoryWithTags> getTagCatalog();

    List<UUID> getUserTags(UUID userId);
    List<String> getUserTagsNames(UUID userId);

    List<UUID> getUserFriends(UUID userId);
    User addFriendToStudent(UUID userId, UUID friendId);
    User removeFriendFromStudent(UUID userId, UUID friendId);

    List<User> getUsersByIds(List<UUID> ids);

}
