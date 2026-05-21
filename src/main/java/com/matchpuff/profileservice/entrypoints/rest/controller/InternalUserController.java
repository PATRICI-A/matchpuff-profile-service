package com.matchpuff.profileservice.entrypoints.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.matchpuff.profileservice.application.dto.request.ResetPasswordRequest;
import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileDto;
import com.matchpuff.profileservice.application.service.InternalUserServicePort;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class InternalUserController {
    private final InternalUserServicePort internalUserService;

    @GetMapping("/users/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserAuthResponse> getUser(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(internalUserService.getUser(userId));
    }

    @GetMapping("/users/mail/{email}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by email")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserAuthResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(internalUserService.getUserByEmail(email));
    }

    @PatchMapping("/users/{userId}/verify")
    @Tag(name = "Users - Update", description = "Update user information")
    @Operation(summary = "Verify user")
    @ApiResponse(responseCode = "204", description = "User verified successfully")
    public ResponseEntity<Void> verifyUser(
            @PathVariable UUID userId) {
        internalUserService.verifyUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{userId}/password")
    @Tag(name = "Users - Update", description = "Update user information")
    @Operation(summary = "Reset user password (forgot password flow)")
    @ApiResponse(responseCode = "204", description = "Password updated successfully")
    public ResponseEntity<Void> resetPassword(
            @PathVariable UUID userId,
            @Valid @RequestBody ResetPasswordRequest request) {
        internalUserService.resetPassword(userId, request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/matching/profiles/{id}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user profile for matching by ID")
    @ApiResponse(responseCode = "200", description = "User profile retrieved successfully")
    public UserMatchProfileDto getProfileForMatching(
            @PathVariable UUID id
    ) {
        return internalUserService.getProfileForMatching(id);
    }

    @GetMapping("/matching/profiles")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all user profiles for matching")
    @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    public List<UserMatchProfileDto> getAllProfilesForMatching() {
        return internalUserService.getAllProfilesForMatching();
    }
}
