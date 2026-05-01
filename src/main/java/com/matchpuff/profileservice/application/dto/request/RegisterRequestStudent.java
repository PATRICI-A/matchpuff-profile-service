package com.matchpuff.profileservice.application.dto.request;

import java.time.LocalDate;

import com.matchpuff.profileservice.domain.valueobjects.CareerEnum;

import jakarta.validation.constraints.Email;

public class RegisterRequestStudent {
    @Email
    private String mail;
    private String password;
    private String name;
    private CareerEnum career;
    private int semester;
    //private String photoUrl;
    private String biography;
    private String[] tags;
    private LocalDate dateOfBirth;

}
