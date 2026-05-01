package com.matchpuff.profileservice.application.dto.request;

import com.matchpuff.profileservice.domain.valueobjects.PrivacyLevelEnum;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateUserRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Size(min = 2, max = 50)
    private String lastName;

    @Size(max = 200, message = "La biografía no puede superar 200 caracteres")
    private String biography;

    private String photo;

    private PrivacyLevelEnum privacyLevel;

    private List<String> interests;

    private List<ScheduleRequest> schedule;


    private String contact;
}
