package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.matchpuff.profileservice.domain.model.enums.GenderEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "users")
@Data
@NoArgsConstructor
public abstract class UserDocument {

    @Id
    private UUID id;

    private String name;

    private String email;

    @Field("passwordHash")
    private String passwordHash;

    @CreatedDate
    private LocalDateTime createdAt;
    
    private GenderEnum gender;    
    
    @Field("userType")
    private UserType userType;

    @Field("verified")
    private boolean verified = false;
    
}