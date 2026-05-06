package com.matchpuff.profileservice.entrypoints.advice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.exceptions.UserAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;


    @BeforeEach
    void setUp() {
        // no setip
    }

    @Test
    void givenValidationError_whenHandleValidationErrors_thenReturnsBadRequest() {
        // Given
        FieldError fieldError = new FieldError("UserStudentRequest", "email", "Invalid email format");
        List<FieldError> errors = new ArrayList<>();
        errors.add(fieldError);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(errors);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("email"));
        assertEquals(400, response.getBody().getStatus());
    }

    @Test
    void givenEmptyValidationErrors_whenHandleValidationErrors_thenReturnsDefaultMessage() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error", response.getBody().getMessage());
    }

    @Test
    void givenUserAlreadyExistsException_whenHandleBusinessErrors_thenReturnsConflict() {
        // Given
        ProfileServiceException ex = new UserAlreadyExistsException("User already exists");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User already exists", response.getBody().getMessage());
        assertEquals(409, response.getBody().getStatus());
    }

    @Test
    void givenInvalidInputException_whenHandleBusinessErrors_thenReturnsBadRequest() {
        // Given
        ProfileServiceException ex = new InvalidInputException("Invalid input");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().getMessage());
        assertEquals(400, response.getBody().getStatus());
    }

    @Test
    void givenGeneralException_whenHandleGeneralErrors_thenReturnsInternalServerError() {
        // Given
        Exception ex = new RuntimeException("Unexpected error");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    void givenNullPointerException_whenHandleGeneralErrors_thenReturnsInternalServerError() {
        // Given
        Exception ex = new NullPointerException("Null pointer encountered");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
    }
}
