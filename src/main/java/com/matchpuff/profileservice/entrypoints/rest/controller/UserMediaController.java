package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import com.matchpuff.profileservice.entrypoints.advice.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserMediaController {
    private final UserServicePort userService;

    @PostMapping(
            value = "/{userId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Tag(name = "User Profiles", description = "Manage user profiles and content. Includes operations to upload images, manage schedules and tags, and retrieve profile-related assets.")
    @Operation(summary = "Upload or update the profile image of the user",
            description = "Uploads a new profile image for the user. Accepts a multipart file parameter named 'file'. The endpoint validates file readability and content type. Returns the updated user profile response including the profile photo URL on success.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile image updated successfully. Returns UserResponseProfilePhoto containing the new image URL and metadata.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseProfilePhoto.class), examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Jane Doe\", \"email\": \"jane@example.com\", \"profileImageUrl\": \"https://cdn.example.com/photos/jane.jpg\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request: file is missing, unreadable or invalid format.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"It was not possible to read the file. Please try again.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required to upload profile image.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token missing.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient permissions to update this user's image.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: cannot update profile image for this user.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while processing or storing the image.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Server error while storing image.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<UserResponseProfilePhoto> uploadProfileImage(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.updateProfileImage(userId, file.getBytes(), file.getContentType()));
        } catch (IOException e) {
            throw new InvalidInputException("It was not possible to read the file. Please try again.");
        }
    }

    @GetMapping("/{userId}/profile-image")
    @Tag(name = "User Profiles", description = "Manage user profiles and content. Includes operations to upload images, manage schedules and tags, and retrieve profile-related assets.")
    @Operation(summary = "Retrieve the profile image of the user",
            description = "Redirects to the stored profile image URL for the given user. If the user has a valid external URL for the photo, the endpoint responds with an HTTP redirect (302) to that location. Use this when clients need to fetch the binary image resource.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Found: response contains a Location header redirecting to the image URL."),
            @ApiResponse(responseCode = "400", description = "Bad request: user does not have a valid profile image or the stored URL is malformed.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"This user does not have a valid profile image.\", \"status\": 400, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: authentication required to access this resource.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Unauthorized: token missing.\", \"status\": 401, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden: insufficient permissions to view the profile image.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Forbidden: cannot view this profile image.\", \"status\": 403, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Not found: user or profile image not available.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Profile image not found.\", \"status\": 404, \"timestamp\": \"2026-05-22T12:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error: error while resolving or redirecting to the image URL.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{\"message\": \"Error resolving image URL.\", \"status\": 500, \"timestamp\": \"2026-05-22T12:00:00\"}")))
    })
    public ResponseEntity<Void> getProfileImage(@PathVariable UUID userId) {
        UserResponse user = userService.getUser(userId);

        if (!(user instanceof StudentProfileResponse student) || student.getPhotoUrl() == null || student.getPhotoUrl().isBlank()) {
            throw new InvalidInputException("This user does not have a profile image.");
        }

        String photoUrl = student.getPhotoUrl();
        if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
            throw new InvalidInputException("This user does not have a valid profile image.");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(photoUrl))
                .build();
    }
}
