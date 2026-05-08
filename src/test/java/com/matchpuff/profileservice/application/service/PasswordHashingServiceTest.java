package com.matchpuff.profileservice.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingServiceTest {

    private PasswordHashingService service;

    @BeforeEach
    void setUp() {
        service = new PasswordHashingService();
    }

    @Test
    void givenRawPassword_whenHashPassword_thenReturnsBCryptHash() {
        String raw = "TestPassword123";

        String hash = service.hashPassword(raw);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));
    }

    @Test
    void givenSamePassword_whenHashedTwice_thenHashesDiffer() {
        String raw = "TestPassword123";

        String hash1 = service.hashPassword(raw);
        String hash2 = service.hashPassword(raw);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void givenMatchingPassword_whenVerifyPassword_thenReturnsTrue() {
        String raw = "TestPassword123";
        String hash = service.hashPassword(raw);

        assertTrue(service.verifyPassword(raw, hash));
    }

    @Test
    void givenWrongPassword_whenVerifyPassword_thenReturnsFalse() {
        String raw = "TestPassword123";
        String hash = service.hashPassword(raw);

        assertFalse(service.verifyPassword("WrongPassword999", hash));
    }
}
