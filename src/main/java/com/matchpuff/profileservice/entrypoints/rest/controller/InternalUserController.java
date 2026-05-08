package com.matchpuff.profileservice.entrypoints.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.service.InternalUserServicePort;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    private final InternalUserServicePort internalUserService;

    @GetMapping("/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by ID")
    public ResponseEntity<UserAuthResponse> getUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(internalUserService.getUser(userId));
    }

    @GetMapping("/mail/{email}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by email")
    public ResponseEntity<UserAuthResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(internalUserService.getUserByEmail(email));
    }

    @PatchMapping("/{userId}/verify")
    @Tag(name = "Users - Update", description = "Update user information")
    @Operation(summary = "Verify user")
    public ResponseEntity<Void> verifyUser(
            @PathVariable String userId) {
        internalUserService.verifyUser(userId);
        return ResponseEntity.noContent().build();
    }

}
