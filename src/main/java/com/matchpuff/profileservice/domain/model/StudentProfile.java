package com.matchpuff.profileservice.domain.model;

import com.matchpuff.profileservice.domain.valueobjects.CareerEnum;
import com.matchpuff.profileservice.domain.valueobjects.PrivacyLevelEnum;

import lombok.Data;

import java.util.List;

@Data
public class StudentProfile extends User {
    private CareerEnum career;
    private int semester;
    //private String photoUrl;
    private String biography;
    private PrivacyLevelEnum privacyLevel;
    private List<Schedule> schedules;
    private List<Tag> tags;

}
