package com.matchpuff.profileservice.infrastructure.adapters.adapter;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.matchpuff.profileservice.domain.exceptions.ImageProfileException;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
import org.springframework.stereotype.Component;

@Component
public class CloudinaryAdapter implements ImageStoragePort {
    private static final Logger log = LoggerFactory.getLogger(CloudinaryAdapter.class);
    private final Cloudinary cloudinary;

    public CloudinaryAdapter(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadProfileImage(byte[] file, UUID userId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file,
                ObjectUtils.asMap(
                    "folder", "profile_pictures",
                    "public_id", userId.toString(),
                    "overwrite", true
                )
            );


            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            log.error("Error uploading profile image for user {}: {}", userId, e.getMessage());
            throw new ImageProfileException("Error uploading profile image. Please try again.");
        }
    }
    

}
