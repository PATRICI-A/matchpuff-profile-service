package com.matchpuff.profileservice.infrastructure.adapters.adapter;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.matchpuff.profileservice.domain.exceptions.ImageProfileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CloudinaryAdapterTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryAdapter adapter;

    @Test
    void givenValidFile_whenUploadProfileImage_thenReturnsSecureUrl() throws Exception {
        byte[] file = new byte[] {1, 2, 3};
        java.util.UUID userId = UUID.randomUUID();

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenReturn(Map.of("secure_url", "https://res.cloudinary.com/test/image/upload/v1/profile.jpg"));

        String result = adapter.uploadProfileImage(file, userId);

        assertEquals("https://res.cloudinary.com/test/image/upload/v1/profile.jpg", result);
        verify(cloudinary).uploader();
        verify(uploader).upload(any(), argThat(options ->
                "profile_pictures".equals(options.get("folder"))
                        && userId.toString().equals(options.get("public_id"))
                        && Boolean.TRUE.equals(options.get("overwrite"))
        ));
    }

    @Test
    void givenCloudinaryFailure_whenUploadProfileImage_thenThrowsImageProfileException() throws Exception {
        byte[] file = new byte[] {1, 2, 3};
        java.util.UUID userId = UUID.randomUUID();

        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), anyMap())).thenThrow(new RuntimeException("boom"));

        assertThrows(ImageProfileException.class, () -> adapter.uploadProfileImage(file, userId));
        verify(cloudinary).uploader();
        verify(uploader).upload(any(), anyMap());
    }
}
