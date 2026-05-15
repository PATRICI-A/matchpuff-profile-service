package com.matchpuff.profileservice.infrastructure.external.matching;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import com.matchpuff.profileservice.domain.exceptions.ExternalServiceException;
import com.matchpuff.profileservice.infrastructure.external.matching.client.MatchingFeignClient;
import com.matchpuff.profileservice.infrastructure.external.matching.dto.CategoryWithTagsDto;
import com.matchpuff.profileservice.infrastructure.external.matching.dto.TagDto;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchingServiceAdapterTest {

    @Mock
    private MatchingFeignClient matchingFeignClient;

    @InjectMocks
    private MatchingServiceAdapter adapter;

    @Test
    void getAllCategoriesWithTags_mapsDtoToDomain() {
        TagDto tag = new TagDto();
        UUID tagId = UUID.randomUUID();
        UUID catId = UUID.randomUUID();
        tag.setId(tagId);
        tag.setName("TagName");
        tag.setCategoryId(catId);

        CategoryWithTagsDto dto = new CategoryWithTagsDto();
        dto.setId(catId);
        dto.setName("CatName");
        dto.setTags(List.of(tag));

        when(matchingFeignClient.getAllCategoriesWithTags()).thenReturn(List.of(dto));

        var result = adapter.getAllCategoriesWithTags();

        assertEquals(1, result.size());
        assertEquals(catId, result.get(0).getId());
        assertEquals("CatName", result.get(0).getName());
        assertEquals(1, result.get(0).getTags().size());
        assertEquals(tagId, result.get(0).getTags().get(0).getId());
    }

    @Test
    void getAllCategoriesWithTags_whenFeignThrows_thenExternalServiceException() {
        FeignException fe = mock(FeignException.class);
        when(matchingFeignClient.getAllCategoriesWithTags()).thenThrow(fe);

        assertThrows(ExternalServiceException.class, () -> adapter.getAllCategoriesWithTags());
    }

    @Test
    void tagExists_returnsTrueWhenFound() {
        UUID id = UUID.randomUUID();
        TagDto tag = new TagDto();
        tag.setId(id);
        when(matchingFeignClient.getTagById(id)).thenReturn(tag);

        assertTrue(adapter.tagExists(id));
    }

    @Test
    void tagExists_returnsFalseWhenNotFound() {
        UUID id = UUID.randomUUID();
        FeignException.NotFound nf = mock(FeignException.NotFound.class);
        when(matchingFeignClient.getTagById(id)).thenThrow(nf);

        assertFalse(adapter.tagExists(id));
    }

    @Test
    void tagExists_whenFeignError_thenExternalServiceException() {
        UUID id = UUID.randomUUID();
        FeignException fe = mock(FeignException.class);
        when(matchingFeignClient.getTagById(id)).thenThrow(fe);

        assertThrows(ExternalServiceException.class, () -> adapter.tagExists(id));
    }

    @Test
    void getTagNameById_returnsName() {
        UUID id = UUID.randomUUID();
        TagDto tag = new TagDto();
        tag.setId(id);
        tag.setName("MyTag");
        when(matchingFeignClient.getTagById(id)).thenReturn(tag);

        assertEquals("MyTag", adapter.getTagNameById(id));
    }

    @Test
    void getTagNameById_notFound_throwsIllegalArgument() {
        UUID id = UUID.randomUUID();
        FeignException.NotFound nf = mock(FeignException.NotFound.class);
        when(matchingFeignClient.getTagById(id)).thenThrow(nf);

        assertThrows(IllegalArgumentException.class, () -> adapter.getTagNameById(id));
    }

    @Test
    void getTagNameById_feignError_throwsExternalServiceException() {
        UUID id = UUID.randomUUID();
        FeignException fe = mock(FeignException.class);
        when(matchingFeignClient.getTagById(id)).thenThrow(fe);

        assertThrows(ExternalServiceException.class, () -> adapter.getTagNameById(id));
    }
}
