package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

@Data
public class UpdateUserRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Email
    private String email;

    @Size(max = 200, message = "La biografía no puede superar 200 caracteres")
    private String biography;

    @NotBlank
    private String photo;

    @NotNull
    private PrivacyLevelEnum privacyLevel;

    @NotEmpty
    private List<String> interests;

    @NotEmpty
    @Valid
    private List<ScheduleRequest> schedule;

}
