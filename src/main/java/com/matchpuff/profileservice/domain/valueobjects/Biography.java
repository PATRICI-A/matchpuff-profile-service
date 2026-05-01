package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class Biography {

    private final String value;

    public Biography(String value) {
        if (value != null && value.length() > 200) {
            throw new InvalidInputException(
                "La biografía no puede superar los 200 caracteres"
            );
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
