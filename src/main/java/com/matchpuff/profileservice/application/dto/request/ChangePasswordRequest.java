package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters long and at least one uppercase letter")
    private String newPassword;
}
