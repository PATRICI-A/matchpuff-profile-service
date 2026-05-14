package com.matchpuff.profileservice.infrastructure.external.matching.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.matchpuff.profileservice.infrastructure.external.matching.dto.CategoryWithTagsDto;
import com.matchpuff.profileservice.infrastructure.external.matching.dto.TagDto;

@FeignClient(
    name = "matching-service",
    url = "${matching.service.url}",
    path = "${matching.service.path}"
)
public interface MatchingFeignClient {

    @GetMapping("/categories/categories-with-tags")
    List<CategoryWithTagsDto> getAllCategoriesWithTags();

    @GetMapping("/categories/tags/{tagId}")
    TagDto getTagById(@PathVariable UUID tagId);
}
