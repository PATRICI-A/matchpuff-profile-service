package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserOrganizerRequest {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[^@]+@escuelaing\\.edu\\.co$", message = "Email must be a valid @escuelaing.edu.co address")
    @Schema(example = "usuario@escuelaing.edu.co")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    @Schema(example = "TestPassword123")
    private String password;

    @NotNull(message = "Gender is required")
    private GenderEnum gender;

    @NotBlank(message = "Contact information is required")
    private String contactInfo;
}
