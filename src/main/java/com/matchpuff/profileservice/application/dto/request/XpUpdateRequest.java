package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class XpUpdateRequest {

    @NotNull(message = "XP value must not be null")
    @Min(value = 0, message = "XP must be zero or positive")
    private Integer xp;
}
