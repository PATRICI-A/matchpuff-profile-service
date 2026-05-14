package com.matchpuff.profileservice.application.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWithTagsResponse {
    private UUID id;
    private String name;
    private List<TagSummaryResponse> tags;
}
