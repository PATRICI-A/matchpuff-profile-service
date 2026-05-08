package com.matchpuff.profileservice.domain.exceptions;
import org.springframework.http.HttpStatus;


public class InvalidInputException extends ProfileServiceException {

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
