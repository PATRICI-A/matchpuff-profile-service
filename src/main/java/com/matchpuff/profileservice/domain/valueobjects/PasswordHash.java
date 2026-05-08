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

        if (!containsUppercaseLetter(value)) {
            throw new InvalidInputException(
                "The password must contain at least one uppercase letter"
            );
        }

        this.value = value;
    }

    private boolean containsUppercaseLetter(String value) {
        for (int index = 0; index < value.length(); index++) {
            if (Character.isUpperCase(value.charAt(index))) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }
}