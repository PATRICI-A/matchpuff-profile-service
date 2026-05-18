package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.BatchProfileRequest;
import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.ChangePasswordRequest;
import com.matchpuff.profileservice.application.dto.request.GeolocationRequest;
import com.matchpuff.profileservice.application.dto.request.FriendRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.response.BatchProfileResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.application.service.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServicePort userService;
    private final UserMapper userRestMapper;

    @PostMapping("/student")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user — STUDENT")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createStudentUser(
            @Valid @RequestBody UserStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createStudentUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/admin")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user — ADMIN")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createAdminUser(
            @Valid @RequestBody UserAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createAdminUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/organizer")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user — ORGANIZER")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createOrganizerUser(
            @Valid @RequestBody UserOrganizerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createOrganizerUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/batch")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain multiple user profiles by IDs")
    @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    public ResponseEntity<List<BatchProfileResponse>> getUsersByIds(
            @Valid @RequestBody BatchProfileRequest request) {
        return ResponseEntity.ok(userService.getUsersByIds(request.getIds()));
    }

    @GetMapping("/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/mail/{email}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by email")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


    @GetMapping("/student-profiles")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all student profiles")
    @ApiResponse(responseCode = "200", description = "Student profiles retrieved successfully")
    public ResponseEntity<List<UserResponse>> getAllStudentProfiles() {
        return ResponseEntity.ok(userService.getAllStudentProfiles());
    }

    @GetMapping("/{userId}/tags")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user tags")
    @ApiResponse(responseCode = "200", description = "User tags retrieved successfully")
    public ResponseEntity<List<UUID>> getUserTags(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserTags(userId));
    }

    @GetMapping("/{userId}/tags/names")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user tags with names")
    @ApiResponse(responseCode = "200", description = "User tags retrieved successfully")
    public ResponseEntity<List<String>> getUserTagNames( @PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserTagsNames(userId));
    }

    @GetMapping("/{userId}/friends")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user friends")
    @ApiResponse(responseCode = "200", description = "User friends retrieved successfully")
    public ResponseEntity<List<UUID>> getUserFriends(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserFriends(userId));
    }

    @PatchMapping("/{userId}/friends")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add a friend to the student user")
    @ApiResponse(responseCode = "200", description = "Friend added successfully")
    public ResponseEntity<UserResponse> addFriend(
            @PathVariable UUID userId,
            @Valid @RequestBody FriendRequest request) {
        return ResponseEntity.ok(userService.addFriend(userId, request.getFriendId()));
    }

    @PatchMapping("/{userId}/friends/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove a friend from the student user")
    @ApiResponse(responseCode = "200", description = "Friend removed successfully")
    public ResponseEntity<UserResponse> removeFriend(
            @PathVariable UUID userId,
            @Valid @RequestBody FriendRequest request) {
        return ResponseEntity.ok(userService.removeFriend(userId, request.getFriendId()));
    }

    @PatchMapping("/student/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update student user data")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    public ResponseEntity<UserResponse> updateUserStudent(
            @PathVariable UUID userId,
            @Valid @RequestBody UserStudentUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/admin/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update admin user data")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    public ResponseEntity<UserResponse> updateUserAdmin(
            @PathVariable UUID userId,
            @Valid @RequestBody UserAdminUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/organizer/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update organizer user data")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    public ResponseEntity<UserResponse> updateUserOrganizer(
            @PathVariable UUID userId,
            @Valid @RequestBody UserOrganizerUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/password")
    @Tag(name = "User Security", description = "Manage user security settings such as passwords")
    @Operation(summary = "Change user password")
    @ApiResponse(responseCode = "204", description = "Password changed successfully")
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}/schedule")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add availability schedule to student user")
    @ApiResponse(responseCode = "200", description = "Schedule updated successfully")
    public ResponseEntity<UserResponse> updateUserSchedule(
            @PathVariable UUID userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.addSchedule(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/schedule/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove availability schedule from student user")
    @ApiResponse(responseCode = "200", description = "Schedule removed successfully")
    public ResponseEntity<UserResponse> removeUserSchedule(
            @PathVariable UUID userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.removeSchedule(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/tags")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add a tag/interest to the student user")
    @ApiResponse(responseCode = "200", description = "Tags updated successfully")
    public ResponseEntity<UserResponse> updateUserTags(
            @PathVariable UUID userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.addTag(userId, request.getTagId()));
    }

    @PatchMapping("/{userId}/tags/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove a tag/interest from the student user")
    @ApiResponse(responseCode = "200", description = "Tag removed successfully")
    public ResponseEntity<UserResponse> removeUserTag(
            @PathVariable UUID userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.removeTag(userId, request.getTagId()));
    }

    @DeleteMapping("/{userId}")
    @Tag(name = "Users - Deletion", description = "Delete users from the system")
    @Operation(summary = "Delete a user by ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
        value = "/{userId}/profile-image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Upload or update the profile image of the user")
    @ApiResponse(responseCode = "200", description = "Profile image updated successfully")
    public ResponseEntity<UserResponseProfilePhoto> uploadProfileImage(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.updateProfileImage(userId, file.getBytes(), file.getContentType()));
        } catch (IOException e) {
            throw new InvalidInputException("It was not possible to read the file. Please try again.");
        }
    }

    @GetMapping("/tags/catalog")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Get all available tags grouped by category")
    @ApiResponse(responseCode = "200", description = "Tags retrieved successfully")
    public ResponseEntity<List<CategoryWithTagsResponse>> getTagCatalog() {
        return ResponseEntity.ok(userService.getTagCatalog());
    }

    @PatchMapping("/{userId}/geolocation")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Enable or disable geolocation for the user")
    @ApiResponse(responseCode = "200", description = "Geolocation updated successfully")
    public ResponseEntity<UserResponse> updateGeolocation(
            @PathVariable UUID userId,
            @Valid @RequestBody GeolocationRequest request) {
        return ResponseEntity.ok(userService.updateGeolocation(userId, request.isGeolocationEnabled()));
    }

    @GetMapping("/{userId}/profile-image")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Retrieve the profile image of the user")
    @ApiResponse(responseCode = "200", description = "Profile image retrieved successfully")
    public ResponseEntity<Void> getProfileImage(@PathVariable UUID userId) {
        UserResponse user = userService.getUser(userId);

        if (!(user instanceof StudentProfileResponse student) || student.getPhotoUrl() == null || student.getPhotoUrl().isBlank()) {
            throw new InvalidInputException("This user does not have a profile image.");
        }

        String photoUrl = student.getPhotoUrl();
        if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
            throw new InvalidInputException("This user does not have a valid profile image.");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(photoUrl))
                .build();
    }

}
