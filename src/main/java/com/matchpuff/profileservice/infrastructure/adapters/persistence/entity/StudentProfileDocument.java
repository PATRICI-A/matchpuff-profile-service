package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

import java.util.List;

@Data
public class StudentProfileDocument extends UserDocument {
    
    public StudentProfileDocument() {
        super();
        this.setUserType(UserType.STUDENT);
    }
    @Field("career")
    private CareerEnum career;

    @Field("semester")
    private Integer semester;

    @Field("photo")
    private String photo;

    @Field("biography")
    private String biography;

    @Field("privacyLevel")
    private PrivacyLevelEnum privacyLevel;

    @Field("interests")
    private List<TagDocument> interests;

    @Field("schedule")
    private List<ScheduleDocument> schedule;
}
