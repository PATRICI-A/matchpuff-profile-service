package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.entrypoints.rest.mapper.UserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Creación, consulta y actualización de usuarios")
public class UserController {
    private final UserServicePort userService;

    @PostMapping
    @Operation(summary = "Crear usuario — STUDENT, ORGANIZER o ADMIN según el campo role")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createUser(UserRestMapper.toDomain(request)));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserResponseInfo> getUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Actualizar datos del usuario")
    public ResponseEntity<UserResponseInfo> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, UserRestMapper.toDomain(request)));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<UserResponseInfo>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}/schedule")
    @Operation(summary = "Agregar un horario al usuario")
    public ResponseEntity<UserResponseInfo> updateUserSchedule(
            @PathVariable String userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.addSchedule(userId, UserRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/tags")
    @Operation(summary = "Agregar un tag/interés al usuario")
    public ResponseEntity<UserResponseInfo> updateUserTags(
            @PathVariable String userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.addTag(userId, UserRestMapper.toDomain(request)));
    }

}
