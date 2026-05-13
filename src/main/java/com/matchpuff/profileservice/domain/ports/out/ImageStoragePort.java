package com.matchpuff.profileservice.domain.ports.out;

import java.util.UUID;

public interface ImageStoragePort {

    String uploadProfileImage(byte[] file, UUID userId);
}
