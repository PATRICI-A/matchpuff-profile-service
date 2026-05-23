package com.matchpuff.profileservice.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.Arrays;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components().addSecuritySchemes(
                BEARER_AUTH_SCHEME,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            ))
            .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME))
            .info(new Info()
                .title("Matchpuff Profile Service API")
                .version("v1.0.0")
                .description("This microservice is responsible for managing user profiles, including creation, update, deletion, profile image management, and user matching features. It provides endpoints for handling student, admin, and organizer profiles, as well as profile-related metadata and security operations. All endpoints are secured and designed for robust user management workflows."))
            .tags(Arrays.asList(
                new Tag().name("Users - Creation").description("APIs to register new users (students, admins and organizers). Validates incoming payloads, enforces uniqueness and returns created resource details."),
                new Tag().name("Users - Reading").description("APIs to retrieve user and profile information for both external clients and internal service-to-service calls. Includes search, get-by-id and batch retrieval operations."),
                new Tag().name("Users - Updating").description("APIs to update user profile fields and settings such as student details, organizer/admin updates, schedules, XP and level. Input is validated before applying changes."),
                new Tag().name("Users - Update").description("Internal update and management operations including verification flows and password reset endpoints. Typically used by administrative or internal processes."),
                new Tag().name("User Profiles").description("Operations related to profile content such as profile image uploads, schedule management and tag/interest manipulation. Returns profile-focused DTOs."),
                new Tag().name("User Security").description("Endpoints focused on user security, including password changes and security-related validation. Enforces password policies and authentication requirements."),
                new Tag().name("Users - Deletion").description("Endpoint to permanently remove users and associated profile data. This operation is destructive and should be used with caution and proper authorization."),
                new Tag().name("Users - Internal").description("Internal-only endpoints intended for inter-service communication, metrics and lightweight data required by other microservices.")
            ));
    }

}
