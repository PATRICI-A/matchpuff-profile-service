package com.matchpuff.profileservice.domain.exceptions;

import org.springframework.http.HttpStatus;


public class ImageProfileException extends ProfileServiceException {

    public ImageProfileException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
