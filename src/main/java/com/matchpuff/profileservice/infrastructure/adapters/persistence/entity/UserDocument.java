package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.matchpuff.profileservice.domain.model.enums.GenderEnum;

import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "users")
@Data
@NoArgsConstructor
public abstract class UserDocument {

    @Id
    private String id;

    @Field("userType")
    private UserType userType;

    private String name;

    private String email;

    private GenderEnum gender;

    private LocalDateTime birthdate;
    
    @CreatedDate
    private LocalDateTime createdAt;

    

    
}   
