package com.matchpuff.profileservice.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class InvalidInputExceptionTest {

    @Test
    void givenMessage_whenCreated_thenGetMessageReturnsIt() {
        InvalidInputException ex = new InvalidInputException("Dato inválido");
        assertEquals("Dato inválido", ex.getMessage());
    }

    @Test
    void whenCreated_thenStatusIsBadRequest() {
        InvalidInputException ex = new InvalidInputException("Error");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void whenCreated_thenIsProfileServiceException() {
        InvalidInputException ex = new InvalidInputException("Error");
        assertInstanceOf(ProfileServiceException.class, ex);
    }
}
