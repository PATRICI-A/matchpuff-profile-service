package com.matchpuff.profileservice.entrypoints.advice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ErrorResponseTest {

    @Test
    void givenMessageAndStatus_whenConstruct_thenFieldsAreSet() {
        // Given
        String message = "Test error message";
        int status = 400;

        // When
        ErrorResponse errorResponse = new ErrorResponse(message, status);

        // Then
        assertEquals(message, errorResponse.getMessage());
        assertEquals(status, errorResponse.getStatus());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void givenErrorResponse_whenCreated_thenTimestampIsCurrentOrAfter() {
        // Given
        LocalDateTime beforeCreation = LocalDateTime.now();

        // When
        ErrorResponse errorResponse = new ErrorResponse("Error", 500);

        // Then
        LocalDateTime afterCreation = LocalDateTime.now();
        assertTrue(errorResponse.getTimestamp().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(errorResponse.getTimestamp().isBefore(afterCreation.plusSeconds(1)));
    }

    @Test
    void givenErrorResponse_whenGetters_thenReturnCorrectValues() {
        // Given
        String message = "Validation failed";
        int status = 422;

        // When
        ErrorResponse errorResponse = new ErrorResponse(message, status);

        // Then
        assertEquals(message, errorResponse.getMessage());
        assertEquals(status, errorResponse.getStatus());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void givenErrorResponse_whenStatusIs500_thenInternalServerError() {
        // Given & When
        ErrorResponse errorResponse = new ErrorResponse("Server error", 500);

        // Then
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Server error", errorResponse.getMessage());
    }

    @Test
    void givenErrorResponse_whenStatusIs404_thenNotFound() {
        // Given & When
        ErrorResponse errorResponse = new ErrorResponse("Resource not found", 404);

        // Then
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Resource not found", errorResponse.getMessage());
    }
}
