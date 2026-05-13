package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class StudentProfileDocument extends UserDocument {
    
    public StudentProfileDocument() {
        super();
        this.setUserType(UserType.STUDENT);
    }

    @Field("birthdate")
    private LocalDateTime birthdate;

    @Field("career")
    private CareerEnum career;

    @Field("semester")
    private Integer semester;

    @Field("studentCarnet")
    private String studentCarnet;

    @Field("photo")
    private String photourl;

    @Field("biography")
    private String biography;

    @Field("privacyLevel")
    private PrivacyLevelEnum privacyLevel;

    @Field("geolocationEnabled")
    private boolean geolocationEnabled;

    @Field("scheduleAvailability")
    private List<ScheduleDocument> scheduleAvailability;
    
    @Field("tagsId")
    private List<UUID> tagsId;

}
