package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class PasswordHash {

    private final String value;

    public PasswordHash(String value) {
        if (value == null || value.length() < 8) {
            throw new InvalidInputException(
                "The password must be at least 8 characters long"
            );
        }

        if (!value.matches(".*[A-Z].*")) {
            throw new InvalidInputException(
                "The password must contain at least one uppercase letter"
            );
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}