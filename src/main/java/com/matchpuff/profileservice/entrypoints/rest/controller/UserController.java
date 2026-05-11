package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.ChangePasswordRequest;
import com.matchpuff.profileservice.application.dto.request.GeolocationRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.entrypoints.rest.mapper.UserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

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
    private final UserRestMapper userRestMapper;

    @PostMapping("/student")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user — STUDENT")
    public ResponseEntity<UserResponse> createStudentUser(
            @Valid @RequestBody UserStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createStudentUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/admin")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user — ADMIN")
    public ResponseEntity<UserResponse> createAdminUser(
            @Valid @RequestBody UserAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createAdminUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/organizer")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user — ORGANIZER")
    public ResponseEntity<UserResponse> createOrganizerUser(
            @Valid @RequestBody UserOrganizerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createOrganizerUser(userRestMapper.toDomain(request)));
    }

    @GetMapping("/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by ID")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/mail/{email}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by email")
    public ResponseEntity<UserResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }


    @GetMapping("/student-profiles")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all student profiles")
    public ResponseEntity<List<UserResponse>> getAllStudentProfiles() {
        return ResponseEntity.ok(userService.getAllStudentProfiles());
    }

    @PatchMapping("/student/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update student user data")
    public ResponseEntity<UserResponse> updateUserStudent(
            @PathVariable String userId,
            @Valid @RequestBody UserStudentUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/admin/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update admin user data")
    public ResponseEntity<UserResponse> updateUserAdmin(
            @PathVariable String userId,
            @Valid @RequestBody UserAdminUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/organizer/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update organizer user data")
    public ResponseEntity<UserResponse> updateUserOrganizer(
            @PathVariable String userId,
            @Valid @RequestBody UserOrganizerUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/password")
    @Tag(name = "User Security", description = "Manage user security settings such as passwords")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}/schedule")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add availability schedule to student user")
    public ResponseEntity<UserResponse> updateUserSchedule(
            @PathVariable String userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.addSchedule(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/schedule/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove availability schedule from student user")
    public ResponseEntity<UserResponse> removeUserSchedule(
            @PathVariable String userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.removeSchedule(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/tags")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add a tag/interest to the student user")
    public ResponseEntity<UserResponse> updateUserTags(
            @PathVariable String userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.addTag(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/tags/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove a tag/interest from the student user")
    public ResponseEntity<UserResponse> removeUserTag(
            @PathVariable String userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.removeTag(userId, userRestMapper.toDomain(request)));
    }

    @DeleteMapping("/{userId}")
    @Tag(name = "Users - Deletion", description = "Delete users from the system")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
        value = "/{userId}/profile-image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Upload or update the profile image of the user")
    public ResponseEntity<UserResponseProfilePhoto> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.updateProfileImage(userId, file.getBytes(), file.getContentType()));
        } catch (IOException e) {
            throw new InvalidInputException("It was not possible to read the file. Please try again.");
        }
    }

    @PatchMapping("/{userId}/geolocation")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Enable or disable geolocation for the user")
    public ResponseEntity<UserResponse> updateGeolocation(
            @PathVariable String userId,
            @Valid @RequestBody GeolocationRequest request) {
        return ResponseEntity.ok(userService.updateGeolocation(userId, request.isGeolocationEnabled()));
    }

    @GetMapping("/{userId}/profile-image")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Retrieve the profile image of the user")
    public ResponseEntity<Void> getProfileImage(@PathVariable String userId) {
        UserResponse user = userService.getUser(userId);

        if (!(user instanceof StudentProfileResponse student) || student.getPhotoUrl() == null || student.getPhotoUrl().isBlank()) {
            throw new InvalidInputException("This user does not have a profile image.");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(student.getPhotoUrl()))
                .build();
    }

}
