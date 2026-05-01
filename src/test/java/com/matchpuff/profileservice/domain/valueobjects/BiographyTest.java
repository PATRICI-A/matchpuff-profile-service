package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiographyTest {

    @Test
    void givenNullBiography_whenCreated_thenGetValueReturnsNull() {
        Biography bio = new Biography(null);
        assertNull(bio.getValue());
    }

    @Test
    void givenShortBiography_whenCreated_thenGetValueReturnsIt() {
        Biography bio = new Biography("Soy estudiante de ingeniería.");
        assertEquals("Soy estudiante de ingeniería.", bio.getValue());
    }

    @Test
    void givenBiographyOfExactly200Chars_whenCreated_thenNoExceptionThrown() {
        String text = "a".repeat(200);
        Biography bio = new Biography(text);
        assertEquals(200, bio.getValue().length());
    }

    @Test
    void givenBiographyOf201Chars_whenCreated_thenThrowsInvalidInputException() {
        String text = "a".repeat(201);
        assertThrows(InvalidInputException.class, () -> new Biography(text));
    }

    @Test
    void givenEmptyBiography_whenCreated_thenGetValueReturnsEmpty() {
        Biography bio = new Biography("");
        assertEquals("", bio.getValue());
    }
}
