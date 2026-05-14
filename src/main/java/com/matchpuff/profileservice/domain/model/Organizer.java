package com.matchpuff.profileservice.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Organizer extends User {
    private String contactInfo;
}
