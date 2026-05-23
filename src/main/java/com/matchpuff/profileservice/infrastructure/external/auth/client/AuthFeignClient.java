package com.matchpuff.profileservice.infrastructure.external.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.matchpuff.profileservice.infrastructure.external.auth.dto.InitVerificationRequestDto;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}",
        path = "${auth.service.path}"
)
public interface AuthFeignClient {

    @PostMapping("/init-verification")
    void initVerification(@RequestBody InitVerificationRequestDto request);
}
