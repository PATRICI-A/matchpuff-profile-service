package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void close() {
        validatorFactory.close();
    }

    private Set<ConstraintViolation<ChangePasswordRequest>> validate(String current, String nw) {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword(current);
        req.setNewPassword(nw);
        return validator.validate(req);
    }

    @Test
    void validPassword_examplesShouldPass() {
        // 8 chars, contains uppercase and special
        assertTrue(validate("curPass1", "Aaaaaaa!").isEmpty());
        assertTrue(validate("curPass1", "Password!").isEmpty());
        assertTrue(validate("curPass1", "GoodPass@").isEmpty());
        // longer password also valid
        assertTrue(validate("curPass1", "VeryLongPasswordWith#").isEmpty());
    }

    @Test
    void missingUppercase_shouldFail() {
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validate("curPass1", "password!");
        assertFalse(violations.isEmpty());
        boolean found = violations.stream().anyMatch(v -> "newPassword".equals(v.getPropertyPath().toString()));
        assertTrue(found);
    }

    @Test
    void missingSpecialCharacter_shouldFail() {
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validate("curPass1", "Password1");
        assertFalse(violations.isEmpty());
        boolean found = violations.stream().anyMatch(v -> "newPassword".equals(v.getPropertyPath().toString()));
        assertTrue(found);
    }

    @Test
    void tooShort_shouldFail() {
        // 4 chars only
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validate("curPass1", "Pw!a");
        assertFalse(violations.isEmpty());
        boolean found = violations.stream().anyMatch(v -> "newPassword".equals(v.getPropertyPath().toString()));
        assertTrue(found);
    }

    @Test
    void patternMessage_presentOnViolation() {
        Set<ConstraintViolation<ChangePasswordRequest>> violations = validate("curPass1", "password");
        assertFalse(violations.isEmpty());
        ConstraintViolation<ChangePasswordRequest> v = violations.stream()
                .filter(x -> "newPassword".equals(x.getPropertyPath().toString()))
                .findFirst().orElse(null);
        assertNotNull(v);
        assertTrue(v.getMessage().contains("Password must be at least 8 characters"));
    }
}
