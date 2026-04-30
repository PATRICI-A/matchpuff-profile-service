package infrastructure.adapters.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDocument {
    @Field("day_of_week")
    private DayOfWeek dayOfWeek;

    @Field("name")
    private String name;

    @Field("start_hour")
    private LocalTime startHour;

    @Field("finish_hour")
    private LocalTime finishHour;
}
