package com.matchpuff.profileservice.application.dto.request;

import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.valueobjects.CareerEnum;
import com.matchpuff.profileservice.domain.valueobjects.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.model.Tag;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    @Size(min = 2, max = 50)
    private String name;

    @Size(min = 2, max = 50)
    private String lastName;

    @Email
    private String email;


    private CareerEnum carreer;

    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    private Integer semester;

    private String photo;

    @Size(max = 200, message = "La biografía no puede superar 200 caracteres")
    private String biography;

    private PrivacyLevelEnum privacyLevel;

    private List<Tag> tags;

    private List<Schedule> schedules;

    private String contact;

}
