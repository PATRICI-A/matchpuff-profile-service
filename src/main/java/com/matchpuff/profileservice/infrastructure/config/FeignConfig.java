package com.matchpuff.profileservice.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
        "com.matchpuff.profileservice.infrastructure.external.matching.client",
        "com.matchpuff.profileservice.infrastructure.external.auth.client"
})
public class FeignConfig {
}
