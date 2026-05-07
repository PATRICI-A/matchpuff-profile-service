package com.matchpuff.profileservice.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.valueobjects.Email;
import com.matchpuff.profileservice.domain.valueobjects.PasswordHash;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;

import lombok.Data;

@Data
public class User {
    private UUID id;
    private String name;
    private Email email;
    private PasswordHash passwordHash;
    private GenderEnum gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;


    public void setEmail(String email){
        this.email = new Email(email);
    }

    public void setName(String name) {
        if (name == null || name.trim().length() < 2 || name.trim().length() > 50) {
            throw new InvalidInputException("Name must be between 2 and 50 characters");
        }
        this.name = name;
    }

    public String getEmail(){
        return email != null ? email.getValue() : null;
    }

    public void setPasswordHash(String passwordHash){
        if (passwordHash != null && !passwordHash.isEmpty()) {
            this.passwordHash = new PasswordHash(passwordHash);
        } else {
            this.passwordHash = null;
        }
    }

    public String getPasswordHash(){
        return passwordHash != null ? passwordHash.getValue() : null;
    }

    public void setGender(GenderEnum gender) {
        if (gender == null) {
            throw new InvalidInputException("Gender must not be null");
        }
        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null && !dateOfBirth.isBefore(LocalDate.now())) {
            throw new InvalidInputException("Date of birth must be a past date");
        }
        this.dateOfBirth = dateOfBirth;
    }
}
