package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalTime;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;

@Data
public class ScheduleRequest {

	@NotNull
	private DayOfWeekEnum dayOfWeek;

	@NotBlank
	private String name;

	@NotNull
	private LocalTime startTime;

	@NotNull
	@PastOrPresent
	private LocalTime endTime;
}
