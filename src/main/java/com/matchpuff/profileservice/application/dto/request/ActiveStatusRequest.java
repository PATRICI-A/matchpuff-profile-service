package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActiveStatusRequest {

    @NotNull(message = "Active status must not be null")
    private Boolean active;
}
