package com.matchpuff.profileservice.domain.ports.out;

public interface ImageStoragePort {

    String uploadProfileImage(byte[] file, String userId);
}
