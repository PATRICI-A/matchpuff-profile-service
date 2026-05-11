package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GeolocationRequest {
    
    @NotNull(message = "Geolocation enabled field must not be null")
    private boolean geolocationEnabled;

    
}
