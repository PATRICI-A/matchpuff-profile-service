package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class StudentCarnet {

    private final String value;

    public StudentCarnet(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException("The carnet cannot be null or empty");
        }
        if (!value.matches("\\d{10}")) {
            throw new InvalidInputException("The carnet must have exactly 10 digits");
        }
        
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
