package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class TagRequest {

    @NotNull(message = "The tag ID cannot be null")
    private UUID tagId;

}
