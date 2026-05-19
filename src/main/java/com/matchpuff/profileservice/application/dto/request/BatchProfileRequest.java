package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BatchProfileRequest {
    @NotEmpty(message = "The list of IDs must not be empty")
    private List<UUID> ids;
}
