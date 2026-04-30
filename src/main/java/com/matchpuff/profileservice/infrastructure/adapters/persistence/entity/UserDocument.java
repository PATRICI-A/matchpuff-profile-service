package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public abstract class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

}
