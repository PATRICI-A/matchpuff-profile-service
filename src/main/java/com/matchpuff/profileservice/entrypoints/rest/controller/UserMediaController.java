package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Upload or update the profile image of the user")
    @ApiResponse(responseCode = "200", description = "Profile image updated successfully")
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
    @Tag(name = "User Profiles", description = "Manage user profiles and content")
    @Operation(summary = "Retrieve the profile image of the user")
    @ApiResponse(responseCode = "200", description = "Profile image retrieved successfully")
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
