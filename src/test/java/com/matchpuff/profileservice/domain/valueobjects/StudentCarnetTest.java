package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentCarnetTest {

    @Test
    void givenValidCarnet_whenCreated_thenGetValueReturnsIt() {
        StudentCarnet carnet = new StudentCarnet(20211234L);
        assertEquals(20211234L, carnet.getValue());
    }

    @Test
    void givenNullCarnet_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new StudentCarnet(null));
    }

    @Test
    void givenZeroCarnet_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new StudentCarnet(0L));
    }

    @Test
    void givenNegativeCarnet_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new StudentCarnet(-1L));
    }
}
