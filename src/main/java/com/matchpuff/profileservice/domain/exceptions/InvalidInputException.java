package com.matchpuff.profileservice.domain.exceptions;

public class InvalidInputException extends ProfileServiceException {

    public InvalidInputException(String message) {
        super(message, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

}
