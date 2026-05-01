package com.matchpuff.profileservice.application.dto.response;

import com.matchpuff.profileservice.domain.valueobjects.CareerEnum;
import com.matchpuff.profileservice.domain.valueobjects.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private CareerEnum career;
    private Integer semester;
    private String biography;
    private PrivacyLevelEnum privacyLevel;
    private List<Tag> interests;
    private List<Schedule> schedule;


    private String contact;
}
