package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UserOrganizerRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Email
    @Pattern(regexp = ".*@escuelaing\\.edu\\.co$")
    @Schema(example = "usuario@escuelaing.edu.co")
    private String email;

    @NotNull
    private GenderEnum gender;

    @NotBlank
    private String contactInfo;
}
