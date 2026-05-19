package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Current password must not be blank")
    private String currentPassword;

    @NotBlank(message = "New password must not be blank")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[!@#$,.]).{8,100}$",
        message = "Password must be at least 8 characters, contain at least one uppercase letter and one of: !@#$,."
    )
    private String newPassword;
}
