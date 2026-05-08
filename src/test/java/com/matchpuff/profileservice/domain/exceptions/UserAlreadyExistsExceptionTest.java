package com.matchpuff.profileservice.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsExceptionTest {

    @Test
    void givenMessage_whenCreated_thenGetMessageReturnsIt() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("Usuario ya existe");
        assertEquals("Usuario ya existe", ex.getMessage());
    }

    @Test
    void whenCreated_thenStatusIsConflict() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("Conflict");
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }

    @Test
    void whenCreated_thenIsProfileServiceException() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("Conflict");
        assertInstanceOf(ProfileServiceException.class, ex);
    }
}
