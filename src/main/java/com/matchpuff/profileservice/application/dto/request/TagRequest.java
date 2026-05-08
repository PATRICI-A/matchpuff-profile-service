package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagRequest {

    @NotBlank(message = "The tag name cannot be blank")
    @Size(min = 1, max = 50, message = "The tag name must be between 1 and 50 characters")
    private String name;

    @NotBlank(message = "The tag category cannot be blank")
    @Size(min = 1, max = 100, message = "The tag category must be between 1 and 100 characters")
    private String category;
}
