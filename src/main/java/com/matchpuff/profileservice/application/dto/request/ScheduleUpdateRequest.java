package com.matchpuff.profileservice.application.dto.request;

import com.matchpuff.profileservice.domain.model.Schedule;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

/**
 * DTO para actualizar el horario de un usuario.
 * Contiene la lista de horarios a asignar.
 */
@Data
public class ScheduleUpdateRequest {
    @Valid
    private List<Schedule> schedules;
}
