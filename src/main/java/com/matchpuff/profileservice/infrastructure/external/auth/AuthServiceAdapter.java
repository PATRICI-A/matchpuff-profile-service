package com.matchpuff.profileservice.infrastructure.external.auth;

import org.springframework.stereotype.Component;

import com.matchpuff.profileservice.domain.exceptions.ExternalServiceException;
import com.matchpuff.profileservice.domain.ports.out.OtpNotificationPort;
import com.matchpuff.profileservice.infrastructure.external.auth.client.AuthFeignClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceAdapter implements OtpNotificationPort {

    private final AuthFeignClient authFeignClient;

    @Override
    public void sendOtp(String email) {
        try {
            authFeignClient.initVerification(email);
        } catch (FeignException e) {
            throw new ExternalServiceException("Auth service unavailable: " + e.getMessage());
        }
    }
}
