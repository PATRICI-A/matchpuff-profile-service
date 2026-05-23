package com.matchpuff.profileservice.domain.ports.out;

public interface OtpNotificationPort {
    void sendOtp(String email);
}
