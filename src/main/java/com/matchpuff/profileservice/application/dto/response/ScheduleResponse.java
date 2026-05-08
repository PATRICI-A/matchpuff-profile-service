package com.matchpuff.profileservice.application.dto.response;

import java.time.LocalTime;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;

import lombok.Data;

@Data
public class ScheduleResponse {
    private DayOfWeekEnum dayOfWeek;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
}
