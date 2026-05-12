package com.matchpuff.profileservice.domain.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.valueobjects.Biography;
import com.matchpuff.profileservice.domain.valueobjects.StudentCarnet;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

@Data
public class StudentProfile extends User {
    private CareerEnum career;
    private int semester;
    private StudentCarnet studentCarnet;
    private String photoUrl;
    private Biography biography;
    private PrivacyLevelEnum privacyLevel;
    private boolean geolocationEnabled;
    private List<Schedule> schedules;
    private List<UUID> tagsId;


    public void setBiography(String biography) {
        this.biography = new Biography(biography);
    }

    public String getBiography() {
        return biography != null ? biography.getValue() : null;
    }

    public void setStudentCarnet(String studentCarnet) {
        if (studentCarnet != null && !studentCarnet.trim().isEmpty()) {
            this.studentCarnet = new StudentCarnet(studentCarnet);
        } else {
            this.studentCarnet = null;
        }
    }

    public String getStudentCarnet() {
        return studentCarnet != null ? studentCarnet.getValue() : null;
    }

    public void setCareer(CareerEnum career) {
        if (career == null) {
            throw new InvalidInputException("Career must not be null");
        }
        this.career = career;
    }

    public void setSemester(int semester) {
        if (semester != 0 && (semester < 1 || semester > 10)) {
            throw new InvalidInputException("Semester must be between 1 and 10");
        }
        this.semester = semester;
    }

    public void setPhotoUrl(String photoUrl) {
        if (photoUrl == null || photoUrl.isBlank()) {
            throw new InvalidInputException("Photo URL must not be blank");
        }
        this.photoUrl = photoUrl;
    }

    public void setPrivacyLevel(PrivacyLevelEnum privacyLevel) {
        if (privacyLevel == null) {
            throw new InvalidInputException("Privacy level must not be null");
        }
        this.privacyLevel = privacyLevel;
    }
}
