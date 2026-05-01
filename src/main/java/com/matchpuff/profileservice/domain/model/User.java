package com.matchpuff.profileservice.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.matchpuff.profileservice.domain.valueobjects.GenderEnum;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private GenderEnum gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
}
