package com.matchpuff.profileservice.domain.exceptions;

import org.springframework.http.HttpStatus;


public class ProfileServiceException extends RuntimeException {
    
    private final HttpStatus status;

    public ProfileServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}