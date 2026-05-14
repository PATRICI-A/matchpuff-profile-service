package com.matchpuff.profileservice.domain.model;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithTags {
    private UUID id;
    private String name;
    private List<TagInfo> tags;
}
