package com.matchpuff.profileservice.domain.exceptions;
import org.springframework.http.HttpStatus;


public class InvalidImageInputException extends ProfileServiceException {

    public InvalidImageInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
