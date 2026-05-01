package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import com.matchpuff.profileservice.domain.valueobjects.DayOfWeekEnum;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ScheduleDocument {
    @Field("day_of_week")
    private DayOfWeekEnum dayOfWeek;

    @Field("name")
    private String name;

    @Field("start_hour")
    private LocalTime startHour;

    @Field("finish_hour")
    private LocalTime finishHour;
}
