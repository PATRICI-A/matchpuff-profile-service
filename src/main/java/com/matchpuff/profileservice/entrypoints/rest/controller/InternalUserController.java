package com.matchpuff.profileservice.entrypoints.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import com.matchpuff.profileservice.entrypoints.advice.ErrorResponse;

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
    @Tag(name = "Users - Reading", description = "Retrieve user data for reading and internal usage. Includes endpoints intended for inter-service queries that need authentication and profile metadata.")
    @Operation(summary = "Obtain user by ID",
            description = "Returns the user's authentication and profile information identified by UUID. Use for internal service-to-service requests that require authentication payload and minimal profile metadata (id, roles, email, etc.).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully. Response contains a UserAuthResponse with id, roles and essential profile fields.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"email\": \"user@example.com\", \"roles\": [\"STUDENT\"]}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: supplied userId is not a valid UUID or request is malformed.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid UUID format for userId.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid authentication token.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token is missing or invalid.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: caller lacks required permissions to access this internal endpoint.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: insufficient permissions.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: no user exists for the provided id.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found for id: 00000000-0000-0000-0000-000000000000.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected error while retrieving user data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unexpected error while retrieving user.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserAuthResponse> getUser(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(internalUserService.getUser(userId));
    }

    @GetMapping("/users/mail/{email}")
    @Tag(name = "Users - Reading", description = "Retrieve user data by email for internal operations. Suitable for service-to-service lookups based on email identifiers.")
    @Operation(summary = "Obtain user by email",
            description = "Fetches the user's authentication and profile details using an email address. Useful for inter-service lookups when only email is available.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully. Returns a UserAuthResponse with authentication details.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthResponse.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"email\": \"user@example.com\", \"roles\": [\"STUDENT\"]}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: provided email is malformed.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Malformed email provided.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token required.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: no user associated with the provided email.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"No user found with email user@example.com.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected failure while fetching user by email.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Failed to retrieve user by email due to server error.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserAuthResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(internalUserService.getUserByEmail(email));
    }

    @PatchMapping("/users/{userId}/verify")
    @Tag(name = "Users - Update", description = "Manage user verification flows, such as account confirmation and administrative verification. Ensures verification flags are set and propagated where needed.")
    @Operation(summary = "Verify user",
            description = "Marks the user as verified. Intended for account confirmation or administrative verification flows. No response body is returned on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User verified successfully. No content returned."),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId or request format.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid user id provided.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token missing.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient permissions to verify the user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: cannot verify this user.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user with provided id does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error occurred while verifying the user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Verification process failed due to server error.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<Void> verifyUser(
            @PathVariable UUID userId) {
        internalUserService.verifyUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{userId}/password")
    @Tag(name = "Users - Update", description = "Manage user password reset flows, including forgot-password and administrative resets. Enforces password policies and validation rules.")
    @Operation(summary = "Reset user password (forgot password flow)",
            description = "Endpoint to set a new password for a user as part of a forgot-password process. Expects a ResetPasswordRequest with the new password. On success, returns no content.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password updated successfully. No content returned."),
            @ApiResponse(responseCode = "400", description = "Bad request: new password fails validation (e.g., too short, missing complexity).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Password must be at least 8 characters and include a number.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required or invalid token.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token is missing or invalid.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient rights to change the password.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: you do not have permission to change this password.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user with provided id does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found with id: 00000000-0000-0000-0000-000000000000.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected failure while updating password.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unexpected error while updating password.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<Void> resetPassword(
            @PathVariable UUID userId,
            @Valid @RequestBody ResetPasswordRequest request) {
        internalUserService.resetPassword(userId, request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/geolocation")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Check if a student has geolocation enabled")
    @ApiResponse(responseCode = "200", description = "Geolocation status retrieved successfully")
    public ResponseEntity<Boolean> isGeolocationEnabled(@PathVariable UUID userId) {
        return ResponseEntity.ok(internalUserService.isGeolocationEnabled(userId));
    }

    @GetMapping("/users/{userId}/active")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Check if a student is active")
    @ApiResponse(responseCode = "200", description = "Active status retrieved successfully")
    public ResponseEntity<Boolean> isActive(@PathVariable UUID userId) {
        return ResponseEntity.ok(internalUserService.isActive(userId));
    }

    @GetMapping("/matching/profiles/{id}")
    @Tag(name = "Users - Reading", description = "Retrieve profiles used by the matching subsystem")
    @Operation(summary = "Obtain user profile for matching by ID",
            description = "Returns a concise profile tailored for matching algorithms including tags, level and availability. Intended for matching microservices which require lightweight profile representations.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully. Returns a UserMatchProfileDto with matching-relevant fields."),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid profile id format."),
            @ApiResponse(responseCode = "404", description = "Not found: no matching profile for the given id."),
            @ApiResponse(responseCode = "500", description = "Internal server error: failure while preparing matching profile.")
    })
    public UserMatchProfileDto getProfileForMatching(
            @PathVariable UUID id
    ) {
        return internalUserService.getProfileForMatching(id);
    }

    @GetMapping("/matching/profiles")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain all user profiles for matching (unfiltered)")
    @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    public List<UserMatchProfileDto> getAllProfilesForMatching() {
        return internalUserService.getAllProfilesForMatching();
    }

    @GetMapping("/matching/profiles/candidates/{userId}")
    @Tag(name = "Users - Reading", description = "Obtain information about users")
    @Operation(summary = "Obtain matching candidates for a user (excludes self, friends and private profiles)")
    @ApiResponse(responseCode = "200", description = "User profiles retrieved successfully")
    public List<UserMatchProfileDto> getMatchingCandidates(
            @PathVariable UUID userId) {
        return internalUserService.getAllProfilesForMatching(userId);
    }
}
