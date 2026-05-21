package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.application.service.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserCreationController {
    private final UserServicePort userService;
    private final UserMapper userRestMapper;

    @PostMapping("/student")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user - STUDENT")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createStudentUser(
            @Valid @RequestBody UserStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createStudentUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/admin")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user - ADMIN")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createAdminUser(
            @Valid @RequestBody UserAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createAdminUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/organizer")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user - ORGANIZER")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<UserResponse> createOrganizerUser(
            @Valid @RequestBody UserOrganizerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createOrganizerUser(userRestMapper.toDomain(request)));
    }
}
