package com.matchpuff.profileservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BatchProfileResponse {
    private UUID id;
    private String name;
    private String email;
    private String biography;
    private String photoUrl;
}
