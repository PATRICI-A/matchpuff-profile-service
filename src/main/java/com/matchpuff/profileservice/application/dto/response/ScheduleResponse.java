package com.matchpuff.profileservice.application.dto.response;

import java.time.LocalTime;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class ScheduleResponse {
	@Schema(description = "Allowed values: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY")
	private DayOfWeekEnum dayOfWeek;
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
}
