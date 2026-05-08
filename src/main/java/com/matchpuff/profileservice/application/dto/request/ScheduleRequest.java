package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalTime;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;

@Data
public class ScheduleRequest {

	@NotNull
	private DayOfWeekEnum dayOfWeek;

	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 100)
	private String name;

	@NotNull(message = "Start time is required")
	private LocalTime startTime;

	@NotNull(message = "End time is required")
	private LocalTime endTime;
}
