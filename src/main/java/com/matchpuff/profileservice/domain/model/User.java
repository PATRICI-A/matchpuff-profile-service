package com.matchpuff.profileservice.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.valueobjects.Email;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private Email email;
    private GenderEnum gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;


    public void setEmail(String email){
        this.email = new Email(email);
    }

    public String getEmail(){
        return email != null ? email.getValue() : null;
    }
}
