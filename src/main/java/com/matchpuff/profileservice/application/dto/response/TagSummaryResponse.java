package com.matchpuff.profileservice.application.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagSummaryResponse {
    private UUID id;
    private String name;
    private UUID categoryId;
}
