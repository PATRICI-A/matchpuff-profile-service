package com.matchpuff.profileservice.infrastructure.external.matching.dto;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDto {
    private UUID id;
    private String name;
    private UUID categoryId;
}
