package com.matchpuff.profileservice.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends ProfileServiceException {
    public ExternalServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
