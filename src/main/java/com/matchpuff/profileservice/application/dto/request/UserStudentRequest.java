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
    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[^@]+@mail\\.escuelaing\\.edu\\.co$", message = "Email must be a valid @mail.escuelaing.edu.co address")
    @Schema(example = "usuario@mail.escuelaing.edu.co")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[!@#$,.]).{8,100}$",
        message = "Password must be at least 8 characters, contain at least one uppercase letter and one of: !@#$,."
    )
    @Schema(example = "TestPassword1!")
    private String password;

    @NotNull(message = "Gender is required")
    @Schema(description = "Allowed values: MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY")
    private GenderEnum gender;

    @NotNull(message = "Career is required")
    @Schema(description = "Allowed values: SYSTEMS_ENGINEERING, CIVIL_ENGINEERING, INDUSTRIAL_ENGINEERING, ELECTRONIC_ENGINEERING, ELECTRICAL_ENGINEERING, MECHANICAL_ENGINEERING, BIOMEDICAL_ENGINEERING, ENVIRONMENTAL_ENGINEERING, STATISTICAL_ENGINEERING, BIOTECHNOLOGY_ENGINEERING, ARTIFICIAL_INTELLIGENCE_ENGINEERING, CYBERSECURITY_ENGINEERING, COMPUTER_SCIENCE, MATHEMATICS, DATA_SCIENCE, BUSINESS_ADMINISTRATION, ECONOMICS, INFORMATION_TECHNOLOGY")
    private CareerEnum career;

    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    private Integer semester;

    @NotNull(message = "Student carnet is required")
    @Pattern(regexp = "\\d{10}", message = "The carnet must have exactly 10 digits")
    private String studentCarnet;

    @NotBlank(message = "Photo URL is required")
    private String photourl;

    @Size(max = 200, message = "The biography cannot exceed 200 characters")
    private String biography;

    @NotNull(message = "Privacy level is required")
    @Schema(description = "Allowed values: PUBLIC, PRIVATE, MATCH_ONLY")
    private PrivacyLevelEnum privacyLevel;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Geolocation enabled is required")
    private Boolean geolocationEnabled;
}
