package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.ActiveStatusRequest;
import com.matchpuff.profileservice.application.dto.request.ChangePasswordRequest;
import com.matchpuff.profileservice.application.dto.request.FriendRequest;
import com.matchpuff.profileservice.application.dto.request.GeolocationRequest;
import com.matchpuff.profileservice.application.dto.request.LevelUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.XpUpdateRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.application.service.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserUpdateController {
    private final UserServicePort userService;
    private final UserMapper userRestMapper;

    @PostMapping("/{userId}/friends")
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

    @PatchMapping("/{userId}/geolocation")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Enable or disable geolocation for the user")
    @ApiResponse(responseCode = "200", description = "Geolocation updated successfully")
    public ResponseEntity<UserResponse> updateGeolocation(
            @PathVariable UUID userId,
            @Valid @RequestBody GeolocationRequest request) {
        return ResponseEntity.ok(userService.updateGeolocation(userId, request.isGeolocationEnabled()));
    }

    @PatchMapping("/{userId}/xp")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Set the XP of a student")
    @ApiResponse(responseCode = "200", description = "XP updated successfully")
    public ResponseEntity<UserResponse> updateXp(
            @PathVariable UUID userId,
            @Valid @RequestBody XpUpdateRequest request) {
        return ResponseEntity.ok(userService.updateXp(userId, request.getXp()));
    }

    @PatchMapping("/{userId}/level")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Set the level of a student")
    @ApiResponse(responseCode = "200", description = "Level updated successfully")
    public ResponseEntity<UserResponse> updateLevel(
            @PathVariable UUID userId,
            @Valid @RequestBody LevelUpdateRequest request) {
        return ResponseEntity.ok(userService.updateLevel(userId, request.getLevel()));
    }

    @PatchMapping("/{userId}/active")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Set the active status of a student")
    @ApiResponse(responseCode = "200", description = "Active status updated successfully")
    public ResponseEntity<UserResponse> updateActiveStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody ActiveStatusRequest request) {
        return ResponseEntity.ok(userService.updateActiveStatus(userId, request.getActive()));
    }
}
