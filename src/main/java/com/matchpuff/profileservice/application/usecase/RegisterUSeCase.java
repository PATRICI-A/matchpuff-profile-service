package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.application.dto.request.RegisterRequestStudent;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;

public class RegisterUSeCase {
    private final UserRepositoryPort userRepository;


    public RegisterUSeCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }


    /*public User execute(RegisterRequestStudent request) {
        

    }*/
}
