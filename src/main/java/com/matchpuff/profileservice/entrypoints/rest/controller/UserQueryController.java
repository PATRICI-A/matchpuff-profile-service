package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.BatchProfileRequest;
import com.matchpuff.profileservice.application.dto.response.CategoryWithTagsResponse;
import com.matchpuff.profileservice.application.dto.response.BatchProfileResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.service.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserQueryController {
    private final UserServicePort userService;

    @GetMapping("/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/mail/{email}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user by email")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
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
    public ResponseEntity<List<UUID>> getUserTags(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserTags(userId));
    }

    @GetMapping("/{userId}/tags/names")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user tags with names")
    @ApiResponse(responseCode = "200", description = "User tags retrieved successfully")
    public ResponseEntity<List<String>> getUserTagNames(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserTagsNames(userId));
    }

    @GetMapping("/{userId}/friends")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain user friends")
    @ApiResponse(responseCode = "200", description = "User friends retrieved successfully")
    public ResponseEntity<List<UUID>> getUserFriends(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserFriends(userId));
    }

    @GetMapping
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/batch")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain multiple user profiles by IDs")
    @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    public ResponseEntity<List<BatchProfileResponse>> getUsersByIds(
            @Valid @RequestBody BatchProfileRequest request) {
        return ResponseEntity.ok(userService.getUsersByIds(request.getIds()));
    }

    @GetMapping("/tags/catalog")
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Get all available tags grouped by category")
    @ApiResponse(responseCode = "200", description = "Tags retrieved successfully")
    public ResponseEntity<List<CategoryWithTagsResponse>> getTagCatalog() {
        return ResponseEntity.ok(userService.getTagCatalog());
    }

    @GetMapping("/internal/{userId}/friends-count")
    @Tag(name = "Users - Internal", description = "Internal endpoints for inter-service communication")
    @Operation(summary = "Get total friend count for a user (inter-service)")
    @ApiResponse(responseCode = "200", description = "Friend count retrieved")
    public ResponseEntity<Integer> getConnectionsCount(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserFriends(userId).size());
    }

}
