package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import com.matchpuff.profileservice.domain.valueobjects.GenderEnum;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public abstract class UserDocument {

    @Indexed(unique = true)
    private String id;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    private GenderEnum gender;

    private LocalDateTime birthdate;
}   
