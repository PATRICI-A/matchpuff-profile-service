package com.matchpuff.profileservice.application.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrganizerResponse extends UserResponse {
    private String contactInfo;
}
