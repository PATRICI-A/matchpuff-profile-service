package com.matchpuff.profileservice.domain.model;

import java.time.LocalTime;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;

import lombok.Data;

@Data
public class Schedule {
    private DayOfWeekEnum dayOfWeek;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

    public Schedule(DayOfWeekEnum dayOfWeek, String name, LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new InvalidInputException("Start time must be before end time");
        }
        this.dayOfWeek = dayOfWeek;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
