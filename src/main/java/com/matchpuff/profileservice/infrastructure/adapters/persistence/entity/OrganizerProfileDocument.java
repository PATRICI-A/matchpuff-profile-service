package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class OrganizerProfileDocument extends UserDocument {
    
    public OrganizerProfileDocument() {
        super();
        this.setUserType(UserType.ORGANIZER);
    }
    @Field("contact")
    private String contact;
}
