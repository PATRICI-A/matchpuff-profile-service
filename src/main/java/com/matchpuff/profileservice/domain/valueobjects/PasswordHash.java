package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class PasswordHash {

    private final String value;

    private static final String SPECIAL_CHARS = "!@#$,.";

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

        if (!containsRequiredSpecialChar(value)) {
            throw new InvalidInputException(
                "The password must contain at least one of the following characters: !@#$,."
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

    private boolean containsRequiredSpecialChar(String value) {
        for (int index = 0; index < value.length(); index++) {
            if (SPECIAL_CHARS.indexOf(value.charAt(index)) >= 0) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }
}