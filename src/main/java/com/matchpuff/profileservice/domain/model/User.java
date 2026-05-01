package com.matchpuff.profileservice.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.matchpuff.profileservice.domain.valueobjects.GenderEnum;

import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private GenderEnum gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
}
