package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.matchpuff.profileservice.domain.model.enums.GenderEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserOrganizerUpdateRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Email
    @Pattern(regexp = ".*@(mail\\.)?escuelaing\\.edu\\.co$")
    @Schema(example = "usuario@escuelaing.edu.co")
    private String email;
    @NotNull
    private GenderEnum gender;
    @NotBlank
    private String contactInfo;
}
