package com.matchpuff.profileservice.infrastructure.external.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${auth.service.url}",
        path = "${auth.service.path}"
)
public interface AuthFeignClient {

    @PostMapping("/init-verification")
    void initVerification(@RequestBody String mail);
}
