package com.matchpuff.profileservice.application.dto.request;

import com.matchpuff.profileservice.domain.model.enums.GenderEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserAdminRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[^@]+@escuelaing\\.edu\\.co$", message = "Email must be a valid @escuelaing.edu.co address")
    @Schema(example = "usuario@escuelaing.edu.co")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[!@#$,.]).{8,100}$",
        message = "Password must be at least 8 characters, contain at least one uppercase letter and one of: !@#$,."
    )
    @Schema(example = "TestPassword1!")
    private String password;

    @NotNull(message = "Gender is required")
    private GenderEnum gender;

}
