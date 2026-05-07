package com.matchpuff.profileservice.entrypoints.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.service.InternalUserServicePort;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    private final InternalUserServicePort internalUserService;

    @GetMapping("/{userId}")
    @Tag(name = "Users - Lectura", description = "Obtener información de usuarios")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserAuthResponse> getUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(internalUserService.getUser(userId));
    }

    @GetMapping("/mail/{email}")
    @Tag(name = "Users - Lectura", description = "Obtener información de usuarios")
    @Operation(summary = "Obtener usuario por mail")
    public ResponseEntity<UserAuthResponse> getUserByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(internalUserService.getUserByEmail(email));
    }

    @PatchMapping("/{userId}/verify")
    @Tag(name = "Users - Actualización", description = "Actualizar información de usuarios")
    @Operation(summary = "Verificar usuario")
    public ResponseEntity<Void> verifyUser(
            @PathVariable String userId) {
        internalUserService.verifyUser(userId);
        return ResponseEntity.noContent().build();
    }

}
