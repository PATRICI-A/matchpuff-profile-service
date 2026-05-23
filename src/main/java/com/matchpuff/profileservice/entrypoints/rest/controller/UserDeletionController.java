package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.service.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import com.matchpuff.profileservice.entrypoints.advice.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserDeletionController {
    private final UserServicePort userService;

    @DeleteMapping("/{userId}")
    @Tag(name = "Users - Deletion", description = "Delete users from the system")
    @Operation(summary = "Delete a user by ID",
            description = "Removes a user and associated profile data from the system. Use with caution — this operation is irreversible. Returns no content on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully. No content returned."),
            @ApiResponse(responseCode = "400", description = "Bad request: invalid userId or request format.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Invalid userId format.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token missing.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient permissions to delete this user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: cannot delete this user.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user with the given id does not exist.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"User not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: unexpected failure while deleting the user.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unexpected error while deleting user.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
