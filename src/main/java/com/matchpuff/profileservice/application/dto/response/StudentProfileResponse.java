package com.matchpuff.profileservice.application.dto.response;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Allowed values: SYSTEMS_ENGINEERING, CIVIL_ENGINEERING, INDUSTRIAL_ENGINEERING, ELECTRONIC_ENGINEERING, ELECTRICAL_ENGINEERING, MECHANICAL_ENGINEERING, BIOMEDICAL_ENGINEERING, ENVIRONMENTAL_ENGINEERING, STATISTICAL_ENGINEERING, BIOTECHNOLOGY_ENGINEERING, ARTIFICIAL_INTELLIGENCE_ENGINEERING, CYBERSECURITY_ENGINEERING, COMPUTER_SCIENCE, MATHEMATICS, DATA_SCIENCE, BUSINESS_ADMINISTRATION, ECONOMICS, INFORMATION_TECHNOLOGY")
    private CareerEnum career;
    private int semester;
    private String studentCarnet;
    private String photoUrl;
    private String biography;
    @Schema(description = "Allowed values: PUBLIC, PRIVATE, MATCH_ONLY")
    private PrivacyLevelEnum privacyLevel;
    private boolean geolocationEnabled;
    private List<ScheduleResponse> schedules;
    private List<TagResponse> tags;
    private Integer xp;
    private Integer level;
    private boolean active;

}
