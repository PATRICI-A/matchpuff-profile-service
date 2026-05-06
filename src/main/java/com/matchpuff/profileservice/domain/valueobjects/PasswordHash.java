package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class PasswordHash {

    private final String value;

    public PasswordHash(String value) {
        if (value == null || value.length() < 8) {
            throw new InvalidInputException(
                "La contraseña debe tener al menos 8 caracteres"
            );
        }

        if (!value.matches(".*[A-Z].*")) {
            throw new InvalidInputException(
                "La contraseña debe contener al menos una letra mayúscula"
            );
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}