package com.matchpuff.profileservice.domain.model;

import java.time.LocalTime;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;

import lombok.Data;

@Data
public class Schedule {
    private DayOfWeekEnum dayOfWeek;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
}
