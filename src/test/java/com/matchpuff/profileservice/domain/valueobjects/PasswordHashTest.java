package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashTest {

    @Test
    void givenValidPassword_whenCreated_thenGetValueReturnsIt() {
        PasswordHash pw = new PasswordHash("Test.Password123");
        assertEquals("Test.Password123", pw.getValue());
    }

    @Test
    void givenNullPassword_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new PasswordHash(null));
    }

    @Test
    void givenPasswordShorterThan8_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new PasswordHash("Short1"));
    }

    @Test
    void givenPasswordWithNoUppercase_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new PasswordHash("alllowercase123"));
    }

    @Test
    void givenPasswordWithExactly8CharsAndUppercase_whenCreated_thenNoException() {
        PasswordHash pw = new PasswordHash("Ab.cde123");
        assertEquals("Ab.cde123", pw.getValue());
    }
}
