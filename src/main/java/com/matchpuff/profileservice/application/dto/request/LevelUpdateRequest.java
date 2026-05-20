package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LevelUpdateRequest {

    @NotNull(message = "Level value must not be null")
    @Min(value = 1, message = "Level must be at least 1")
    private Integer level;
}
