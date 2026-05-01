package com.matchpuff.profileservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserResponseInfo {

    private String id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private String userType;

    private String gender;
    private String dateOfBirth;
    private String biography;

    private List<ScheduleResponse> schedules;
    private List<TagResponse> tags;

}
