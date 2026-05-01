package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.matchpuff.profileservice.domain.valueobjects.CareerEnum;
import com.matchpuff.profileservice.domain.valueobjects.PrivacyLevelEnum;

import java.util.List;

@Document(collection = "students")
@Data
@NoArgsConstructor
public class StudentProfileDocument extends UserDocument {
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
