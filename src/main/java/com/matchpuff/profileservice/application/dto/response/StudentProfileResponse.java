package com.matchpuff.profileservice.application.dto.response;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class StudentProfileResponse extends UserResponse {
    private String dateOfBirth;
    private CareerEnum career;
    private int semester;
    private String studentCarnet;
    private String photoUrl;
    private String biography;
    private PrivacyLevelEnum privacyLevel;
    private boolean geolocationEnabled;
    private List<ScheduleResponse> schedules;
    private List<TagResponse> tags;
    private Integer xp;
    private Integer level;
    private boolean active;

}
