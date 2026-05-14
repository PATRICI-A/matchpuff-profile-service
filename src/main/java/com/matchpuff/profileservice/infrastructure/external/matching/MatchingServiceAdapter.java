package com.matchpuff.profileservice.infrastructure.external.matching;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.matchpuff.profileservice.domain.exceptions.ExternalServiceException;
import com.matchpuff.profileservice.domain.model.CategoryWithTags;
import com.matchpuff.profileservice.domain.model.TagInfo;
import com.matchpuff.profileservice.domain.ports.out.TagCatalogPort;
import com.matchpuff.profileservice.infrastructure.external.matching.client.MatchingFeignClient;
import com.matchpuff.profileservice.infrastructure.external.matching.dto.CategoryWithTagsDto;
import com.matchpuff.profileservice.infrastructure.external.matching.dto.TagDto;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchingServiceAdapter implements TagCatalogPort {

    private final MatchingFeignClient matchingFeignClient;

    @Override
    public List<CategoryWithTags> getAllCategoriesWithTags() {
        try {
            return matchingFeignClient.getAllCategoriesWithTags().stream()
                    .map(this::toCategoryWithTags)
                    .toList();
        } catch (FeignException e) {
            throw new ExternalServiceException("Matching service unavailable: " + e.getMessage());
        }
    }

    @Override
    public boolean tagExists(UUID tagId) {
        try {
            matchingFeignClient.getTagById(tagId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            throw new ExternalServiceException("Matching service unavailable: " + e.getMessage());
        }
    }

    private CategoryWithTags toCategoryWithTags(CategoryWithTagsDto dto) {
        List<TagInfo> tags = dto.getTags() == null ? List.of() :
                dto.getTags().stream().map(this::toTagInfo).toList();
        return new CategoryWithTags(dto.getId(), dto.getName(), tags);
    }

    private TagInfo toTagInfo(TagDto dto) {
        return new TagInfo(dto.getId(), dto.getName(), dto.getCategoryId());
    }
}
