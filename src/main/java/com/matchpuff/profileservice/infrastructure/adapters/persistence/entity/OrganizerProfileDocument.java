package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "organizers")
@Data
@NoArgsConstructor
public class OrganizerProfileDocument extends UserDocument {
    @Field("contact")
    private String contact;
}
