package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserStudentRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Email
    @Pattern(regexp = ".*@(mail\\.)?escuelaing\\.edu\\.co$")
    @Schema(example = "usuario@escuelaing.edu.co")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
    @Schema(example = "TestPassword123")
    private String password;

    @NotNull
    private GenderEnum gender;

    @NotNull
    private CareerEnum carreer;

    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    private Integer semester;

    @NotNull
    @Pattern(regexp = "\\d{10}", message = "The carnet must have exactly 10 digits")
    private String studentCarnet;

    @NotBlank
    private String photourl;

    @Size(max = 200, message = "The biography cannot exceed 200 characters")
    private String biography;

    @NotNull
    private PrivacyLevelEnum privacyLevel;

    @NotNull
    @Past
    private LocalDate dateOfBirth;
}
