package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class Biography {

    private final String value;

    public Biography(String value) {
        if (value != null && value.length() > 200) {
            throw new InvalidInputException(
                "The biography cannot exceed 200 characters"
            );
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
