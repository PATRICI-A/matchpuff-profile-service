package com.matchpuff.profileservice.application.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class AdminResponse extends UserResponse {
    // Admin no tiene campos adicionales por ahora
}
