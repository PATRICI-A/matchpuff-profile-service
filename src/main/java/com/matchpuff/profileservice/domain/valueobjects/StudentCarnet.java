package com.matchpuff.profileservice.domain.valueobjects;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

public class StudentCarnet {

    private final Integer value;

    public StudentCarnet(Integer value) {
        if (value == null || value <= 0) {
            throw new InvalidInputException("El carnet del estudiante debe ser un número válido y positivo");
        }

        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
