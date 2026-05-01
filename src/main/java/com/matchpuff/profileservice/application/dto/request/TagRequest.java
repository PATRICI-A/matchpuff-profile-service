package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;
}
