package com.matchpuff.profileservice.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceExceptionTest {

    @Test
    void givenMessageAndStatus_whenCreated_thenGetMessageReturnsIt() {
        ProfileServiceException ex = new ProfileServiceException("Error de prueba", HttpStatus.BAD_REQUEST);
        assertEquals("Error de prueba", ex.getMessage());
    }

    @Test
    void givenMessageAndStatus_whenCreated_thenGetStatusReturnsIt() {
        ProfileServiceException ex = new ProfileServiceException("Error", HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void givenConflictStatus_whenCreated_thenGetStatusReturnsConflict() {
        ProfileServiceException ex = new ProfileServiceException("Conflict", HttpStatus.CONFLICT);
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void givenInternalServerErrorStatus_whenCreated_thenIsRuntimeException() {
        ProfileServiceException ex = new ProfileServiceException("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
        assertInstanceOf(RuntimeException.class, ex);
    }
}
