package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserStudentUpdateRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Email
    @Pattern(regexp = ".*@(mail\\.)?escuelaing\\.edu\\.co$")
    @Schema(example = "usuario@escuelaing.edu.co")
    private String email;

    @NotNull
    private GenderEnum gender;

    @NotNull
    private CareerEnum carreer;

    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    private Integer semester;

    @NotNull
    @Min(value = 10000000, message = "El carnet debe ser un número de al menos 8 dígitos")
    private Long studentCarnet;

    @Size(max = 200, message = "La biografía no puede superar 200 caracteres")
    private String biography;

    @NotNull
    private PrivacyLevelEnum privacyLevel;

    @NotNull
    @Past
    private LocalDate dateOfBirth;
}
