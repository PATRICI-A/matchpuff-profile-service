package com.matchpuff.profileservice.entrypoints.advice;

import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private MethodArgumentNotValidException validationException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // ── handleValidationErrors ─────────────────────────────────────

    @Test
    void givenFieldError_whenHandleValidationErrors_thenReturnsBadRequest() {
        FieldError fieldError = new FieldError("user", "email", "must be valid");
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("email"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenNoFieldErrors_whenHandleValidationErrors_thenReturnsDefaultMessage() {
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error", response.getBody().getMessage());
    }

    @Test
    void givenMultipleFieldErrors_whenHandleValidationErrors_thenReturnsFirstError() {
        FieldError first = new FieldError("user", "name", "must not be blank");
        FieldError second = new FieldError("user", "email", "must be valid");
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(first, second));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("name"));
    }

    // ── handleBusinessErrors ───────────────────────────────────────

    @Test
    void givenNotFoundProfileServiceException_whenHandleBusinessErrors_thenReturns404() {
        ProfileServiceException ex = new ProfileServiceException("User not found", HttpStatus.NOT_FOUND);

        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenConflictProfileServiceException_whenHandleBusinessErrors_thenReturns409() {
        ProfileServiceException ex = new ProfileServiceException("User already exists", HttpStatus.CONFLICT);

        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
    }

    @Test
    void givenBadRequestProfileServiceException_whenHandleBusinessErrors_thenReturns400() {
        ProfileServiceException ex = new ProfileServiceException("Invalid data", HttpStatus.BAD_REQUEST);

        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody().getMessage());
    }

    // ── handleGeneralErrors ────────────────────────────────────────

    @Test
    void givenGenericException_whenHandleGeneralErrors_thenReturns500() {
        Exception ex = new Exception("Unexpected failure");

        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenRuntimeException_whenHandleGeneralErrors_thenReturns500() {
        RuntimeException ex = new RuntimeException("Runtime error");

        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
    }

    @Test
    void givenNullMessageException_whenHandleGeneralErrors_thenReturns500WithDefaultMessage() {
        Exception ex = new Exception((String) null);

        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
    }
}
