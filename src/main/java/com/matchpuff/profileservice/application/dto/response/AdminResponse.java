package com.matchpuff.profileservice.application.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class AdminResponse extends UserResponse {
    // Admin no tiene campos adicionales por ahora
}
