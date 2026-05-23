package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
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
    @Operation(summary = "Create user - STUDENT",
            description = "Creates a new student user using the supplied UserStudentRequest. Validates the payload and returns the created user's public representation. Use this endpoint when registering student profiles.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully. Returns the created UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Jane Doe\", \"email\": \"jane@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: validation failed for input fields (missing or invalid values).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Validation failed: email is required.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required for this operation if applicable.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token is missing.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict: a user with the same unique identifier (email) already exists.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Conflict: user with email jane@example.com already exists.\", \"status\": 409, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected error while creating the user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unexpected error while creating user.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> createStudentUser(
            @Valid @RequestBody UserStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createStudentUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/admin")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user - ADMIN",
            description = "Creates a new admin user. This endpoint expects a UserAdminRequest and returns the created user's details. Admin users usually require elevated permissions and may be created only by other admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully. Returns the created UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Admin User\", \"email\": \"admin@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: validation failed for input fields.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid admin request payload.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: caller must be authenticated and authorized to create admin users.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: authentication required.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient privileges to create an admin user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: only admins can create other admins.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict: an admin user with the same unique identifier already exists.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Conflict: admin with email admin@example.com already exists.\", \"status\": 409, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected error during admin user creation.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error while creating admin user.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> createAdminUser(
            @Valid @RequestBody UserAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createAdminUser(userRestMapper.toDomain(request)));
    }

    @PostMapping("/organizer")
    @Tag(name = "Users - Creation", description = "Create new users")
    @Operation(summary = "Create user - ORGANIZER",
            description = "Creates a new organizer user using UserOrganizerRequest. Organizers can manage events or content and usually require verification steps.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully. Returns the created UserResponse.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Organizer Name\", \"email\": \"org@example.com\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: input validation failed.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid organizer data.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required if creation is restricted.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: authentication required.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict: user with provided unique fields already exists.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Conflict: user with this email already exists.\", \"status\": 409, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected error while creating organizer user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error while creating organizer.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponse> createOrganizerUser(
            @Valid @RequestBody UserOrganizerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createOrganizerUser(userRestMapper.toDomain(request)));
    }
}
