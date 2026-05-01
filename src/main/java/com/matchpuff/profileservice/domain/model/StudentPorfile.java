package com.matchpuff.profileservice.domain.model;

import com.matchpuff.profileservice.domain.valueobjects.CareerEnum;
import com.matchpuff.profileservice.domain.valueobjects.PrivacyLevelEnum;

import lombok.Data;

@Data
public class StudentPorfile extends User {
    private CareerEnum career;
    private int semester;
    private String photoUrl;
    private String biography;
    private PrivacyLevelEnum privacyLevel;
    private Schedule[] schedules;
    private Tag[] tags;

}
