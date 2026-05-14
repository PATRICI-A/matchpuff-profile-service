package com.matchpuff.profileservice.domain.ports.out;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.domain.model.CategoryWithTags;

public interface TagCatalogPort {
    List<CategoryWithTags> getAllCategoriesWithTags();
    boolean tagExists(UUID tagId);
}
