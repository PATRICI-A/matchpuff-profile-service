package com.matchpuff.profileservice.infrastructure.external.matching.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryWithTagsDto {
    private UUID id;
    private String name;
    private List<TagDto> tags;
}
