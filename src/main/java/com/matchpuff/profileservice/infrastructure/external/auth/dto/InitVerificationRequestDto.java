package com.matchpuff.profileservice.infrastructure.external.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitVerificationRequestDto {
    private String email;
}
