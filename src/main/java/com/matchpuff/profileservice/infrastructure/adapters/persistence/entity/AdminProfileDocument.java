package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminProfileDocument extends UserDocument {
    
    public AdminProfileDocument() {
        super();
        this.setUserType(UserType.ADMIN);
    }
    // No extra fields for admin profile
}
