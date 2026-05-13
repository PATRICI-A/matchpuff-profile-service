package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizerProfileDocument extends UserDocument {
    
    public OrganizerProfileDocument() {
        super();
        this.setUserType(UserType.ORGANIZER);
    }
    @Field("contact")
    private String contact;
}
