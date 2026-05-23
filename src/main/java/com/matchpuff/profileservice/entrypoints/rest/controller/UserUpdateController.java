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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import com.matchpuff.profileservice.entrypoints.advice.ErrorResponse;
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
    @Operation(summary = "Add a friend to the student user",
            description = "Adds the provided friendId to the user's friends list. Expects a FriendRequest body with the friend's UUID. Returns the updated user profile on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend added successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Friend User\", \"email\": \"friend@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid friendId or payload.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid friendId provided.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or friend not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User or friend not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict: friend relationship already exists.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Friend relationship already exists.\", \"status\": 409, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while adding friend.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error adding friend.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> addFriend(
            @PathVariable UUID userId,
            @Valid @RequestBody FriendRequest request) {
        return ResponseEntity.ok(userService.addFriend(userId, request.getFriendId()));
    }

    @PatchMapping("/{userId}/friends/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove a friend from the student user",
            description = "Removes the specified friendId from the user's friends list. Returns the updated user profile when removal succeeds.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend removed successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Friend User\", \"email\": \"friend@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid input.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid input.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or friend not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User or friend not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while removing friend.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error removing friend.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> removeFriend(
            @PathVariable UUID userId,
            @Valid @RequestBody FriendRequest request) {
        return ResponseEntity.ok(userService.removeFriend(userId, request.getFriendId()));
    }

    @PatchMapping("/student/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update student user data",
            description = "Updates student-specific profile fields using the provided UserStudentUpdateRequest. Returns the updated user profile. Input is validated before applying changes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Updated Student\", \"email\": \"student@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: validation errors in update payload.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Validation failed for update payload.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to update user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating user.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateUserStudent(
            @PathVariable UUID userId,
            @Valid @RequestBody UserStudentUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/admin/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update admin user data",
            description = "Updates admin-specific profile fields using UserAdminUpdateRequest. Returns the updated admin user profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Updated Admin\", \"email\": \"admin@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: validation errors.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Validation failed.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: admin user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Admin user not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to update admin user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating admin.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateUserAdmin(
            @PathVariable UUID userId,
            @Valid @RequestBody UserAdminUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/organizer/{userId}")
    @Tag(name = "Users - Updating", description = "Update user information")
    @Operation(summary = "Update organizer user data",
            description = "Updates organizer-specific profile fields. Returns the updated organizer profile on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Updated Organizer\", \"email\": \"org@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: validation errors.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Validation failed.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: organizer user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Organizer not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to update organizer user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating organizer.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateUserOrganizer(
            @PathVariable UUID userId,
            @Valid @RequestBody UserOrganizerUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/password")
    @Tag(name = "User Security", description = "Manage user security settings such as passwords")
    @Operation(summary = "Change user password",
            description = "Changes the user's password when provided with the current and new password in ChangePasswordRequest. This endpoint enforces validation and verification of current credentials. No content is returned on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password changed successfully. No content returned."),
            @ApiResponse(responseCode = "400", description = "Bad request: validation error (e.g., new password policy violation).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"New password does not meet complexity rules.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required or current password incorrect.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: current password incorrect.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient rights to change password.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: cannot change password for this user.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failure while updating password.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating password.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<Void> changePassword(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/schedule")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add availability schedule to student user",
            description = "Adds an availability schedule entry to the student's profile. Accepts a ScheduleRequest and returns the updated profile with the new schedule applied.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Schedule updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid schedule data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid schedule format.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed while updating schedule.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating schedule.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateUserSchedule(
            @PathVariable UUID userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.addSchedule(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/schedule/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove availability schedule from student user",
            description = "Removes an availability schedule entry specified in ScheduleRequest from the student's profile. Returns the updated profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Schedule removed successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid schedule data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid schedule data.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or schedule entry not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Schedule entry not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed while removing schedule.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error removing schedule.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> removeUserSchedule(
            @PathVariable UUID userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.removeSchedule(userId, userRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/tags")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Add a tag/interest to the student user",
            description = "Associates an existing tag (by id) to the user's profile. Expects TagRequest containing the tagId. Returns updated user profile containing the new tag.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid tag id or payload.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid tag id.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or tag not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Tag not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict: tag already associated with user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Tag already associated with user.\", \"status\": 409, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while adding tag.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error adding tag.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateUserTags(
            @PathVariable UUID userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.addTag(userId, request.getTagId()));
    }

    @PatchMapping("/{userId}/tags/remove")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Remove a tag/interest from the student user",
            description = "Removes an associated tag from the user's profile. Expects TagRequest specifying tagId. Returns updated profile after removal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag removed successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid tag id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid tag id.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or tag association not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Tag association not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed to remove tag.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error removing tag.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> removeUserTag(
            @PathVariable UUID userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.removeTag(userId, request.getTagId()));
    }

    @PatchMapping("/{userId}/geolocation")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Enable or disable geolocation for the user",
            description = "Toggles the geolocation enabled flag for a user based on GeolocationRequest. Returns the updated profile reflecting the new geolocation setting.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Geolocation updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid input.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid input.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed while updating geolocation.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating geolocation.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateGeolocation(
            @PathVariable UUID userId,
            @Valid @RequestBody GeolocationRequest request) {
        return ResponseEntity.ok(userService.updateGeolocation(userId, request.isGeolocationEnabled()));
    }

    @PatchMapping("/{userId}/xp")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Set the XP of a student",
            description = "Updates the experience points (XP) of a student. Accepts XpUpdateRequest with the new XP value and returns updated profile containing revised XP and derived fields.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "XP updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\", \"xp\": 150}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid XP value.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid XP value.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed while updating XP.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating XP.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateXp(
            @PathVariable UUID userId,
            @Valid @RequestBody XpUpdateRequest request) {
        return ResponseEntity.ok(userService.updateXp(userId, request.getXp()));
    }

    @PatchMapping("/{userId}/level")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Set the level of a student",
            description = "Sets the student's level according to LevelUpdateRequest. Returns updated profile containing the new level and related computed fields.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Level updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\", \"level\": 3}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid level value.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid level value.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed while updating level.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating level.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateLevel(
            @PathVariable UUID userId,
            @Valid @RequestBody LevelUpdateRequest request) {
        return ResponseEntity.ok(userService.updateLevel(userId, request.getLevel()));
    }

    @PatchMapping("/{userId}/active")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Set the active status of a student",
            description = "Enables or disables a student's active status using ActiveStatusRequest containing a boolean. Returns the updated profile reflecting the new active state.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active status updated successfully. Returns updated UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Student\", \"email\": \"s@example.com\", \"active\": true}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid input.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid input.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: failed while updating active status.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error updating active status.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> updateActiveStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody ActiveStatusRequest request) {
        return ResponseEntity.ok(userService.updateActiveStatus(userId, request.getActive()));
    }
}
