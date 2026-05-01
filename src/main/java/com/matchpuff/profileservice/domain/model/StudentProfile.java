package com.matchpuff.profileservice.domain.model;

import lombok.Data;

import java.util.List;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.valueobjects.Biography;

@Data
public class StudentProfile extends User {
    private CareerEnum career;
    private int semester;
    private String photoUrl;
    private Biography biography;
    private PrivacyLevelEnum privacyLevel;
    private List<Schedule> schedules;
    private List<Tag> tags;


    public void setBiography(String biography) {
        this.biography = new Biography(biography);
    }

    public String getBiography() {
        return biography != null ? biography.getValue() : null;
    }
}
