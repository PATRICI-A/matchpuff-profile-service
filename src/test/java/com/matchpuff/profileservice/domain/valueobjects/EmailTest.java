package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void givenValidEscuelaingEmail_whenCreated_thenGetValueReturnsIt() {
        Email email = new Email("user@escuelaing.edu.co");
        assertEquals("user@escuelaing.edu.co", email.getValue());
    }

    @Test
    void givenValidMailEscuelaingEmail_whenCreated_thenGetValueReturnsIt() {
        Email email = new Email("user@mail.escuelaing.edu.co");
        assertEquals("user@mail.escuelaing.edu.co", email.getValue());
    }

    @Test
    void givenNullEmail_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new Email(null));
    }

    @Test
    void givenNonInstitutionalEmail_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new Email("user@gmail.com"));
    }

    @Test
    void givenEmailWithDifferentDomain_whenCreated_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> new Email("user@uniandes.edu.co"));
    }
}
