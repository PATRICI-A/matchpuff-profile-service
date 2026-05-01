package com.matchpuff.profileservice.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ProfileServiceException {

    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
