package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class Email {

    private final String value;

    public Email(String value) {
        if (value == null || (!value.endsWith("@escuelaing.edu.co") && !value.endsWith("@mail.escuelaing.edu.co"))) {
            throw new InvalidInputException("El email debe ser institucional (@escuelaing.edu.co o @mail.escuelaing.edu.co)");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
