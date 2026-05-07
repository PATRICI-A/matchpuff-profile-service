package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.ChangePasswordRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.entrypoints.rest.mapper.UserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServicePort userService;

    @PostMapping("/student")
    @Tag(name = "Users - Creación", description = "Crear nuevos usuarios")
    @Operation(summary = "Crear usuario — STUDENT")
    public ResponseEntity<UserResponse> createStudentUser(
            @Valid @RequestBody UserStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createStudentUser(UserRestMapper.toDomain(request)));
    }

    @PostMapping("/admin")
    @Tag(name = "Users - Creación", description = "Crear nuevos usuarios")
    @Operation(summary = "Crear usuario — ADMIN")
    public ResponseEntity<UserResponse> createAdminUser(
            @Valid @RequestBody UserAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createAdminUser(UserRestMapper.toDomain(request)));
    }

    @PostMapping("/organizer")
    @Tag(name = "Users - Creación", description = "Crear nuevos usuarios")
    @Operation(summary = "Crear usuario — ORGANIZER")
    public ResponseEntity<UserResponse> createOrganizerUser(
            @Valid @RequestBody UserOrganizerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.createOrganizerUser(UserRestMapper.toDomain(request)));
    }

    @GetMapping("/{userId}")
    @Tag(name = "Users - Lectura", description = "Obtener información de usuarios")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/mail")
    @Tag(name = "Users - Lectura", description = "Obtener información de usuarios")
    @Operation(summary = "Obtener usuario por mail")
    public ResponseEntity<UserResponse> getUserByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/student-profiles")
    @Tag(name = "Users - Lectura", description = "Obtener información de usuarios")
    @Operation(summary = "Obtener todos los perfiles de estudiantes")
    public ResponseEntity<List<UserResponse>> getAllStudentProfiles() {
        return ResponseEntity.ok(userService.getAllStudentProfiles());
    }

    @PatchMapping("/student/{userId}")
    @Tag(name = "Users - Actualización", description = "Actualizar información de usuarios")
    @Operation(summary = "Actualizar datos del usuario estudiante")
    public ResponseEntity<UserResponse> updateUserStudent(
            @PathVariable String userId,
            @Valid @RequestBody UserStudentUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, UserRestMapper.toDomain(request)));
    }

    @PatchMapping("/admin/{userId}")
    @Tag(name = "Users - Actualización", description = "Actualizar información de usuarios")
    @Operation(summary = "Actualizar datos del usuario administrador")
    public ResponseEntity<UserResponse> updateUserAdmin(
            @PathVariable String userId,
            @Valid @RequestBody UserAdminUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, UserRestMapper.toDomain(request)));
    }

    @PatchMapping("/organizer/{userId}")
    @Tag(name = "Users - Actualización", description = "Actualizar información de usuarios")
    @Operation(summary = "Actualizar datos del usuario organizador")
    public ResponseEntity<UserResponse> updateUserOrganizer(
            @PathVariable String userId,
            @Valid @RequestBody UserOrganizerUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, UserRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/password")
    @Tag(name = "User Security", description = "Gestionar seguridad de usuarios")
    @Operation(summary = "Cambiar la contraseña de un usuario")
    public ResponseEntity<Void> changePassword(
            @PathVariable String userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Tag(name = "Users - Lectura", description = "Obtener información de usuarios")
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}/schedule")
    @Tag(name = "User Profiles", description = "Gestionar perfiles y contenido de usuarios")
    @Operation(summary = "Agregar un horario al usuario estudiante")
    public ResponseEntity<UserResponse> updateUserSchedule(
            @PathVariable String userId,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(userService.addSchedule(userId, UserRestMapper.toDomain(request)));
    }

    @PatchMapping("/{userId}/tags")
    @Tag(name = "User Profiles", description = "Gestionar perfiles y contenido de usuarios")
    @Operation(summary = "Agregar un tag/interés al usuario estudiante")
    public ResponseEntity<UserResponse> updateUserTags(
            @PathVariable String userId,
            @Valid @RequestBody TagRequest request) {
        return ResponseEntity.ok(userService.addTag(userId, UserRestMapper.toDomain(request)));
    }

    @DeleteMapping("/{userId}")
    @Tag(name = "Users - Eliminación", description = "Eliminar usuarios")
    @Operation(summary = "Eliminar un usuario por ID")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
        value = "/{userId}/profile-image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Tag(name = "User Profiles", description = "Gestionar perfiles y contenido de usuarios")
    @Operation(summary = "Subir o actualizar la imagen de perfil del usuario")
    public ResponseEntity<UserResponseProfilePhoto> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.updateProfileImage(userId, file.getBytes(), file.getContentType()));
        } catch (IOException e) {
            throw new InvalidInputException("It was not possible to read the file. Please try again.");
        }
    }

    @GetMapping("/{userId}/profile-image")
    @Tag(name = "User Profiles", description = "Gestionar perfiles y contenido de usuarios")
    @Operation(summary = "Abrir la imagen de perfil del usuario")
    public ResponseEntity<Void> getProfileImage(@PathVariable String userId) {
        UserResponse user = userService.getUser(userId);

        if (!(user instanceof StudentProfileResponse student) || student.getPhotoUrl() == null || student.getPhotoUrl().isBlank()) {
            throw new InvalidInputException("This user does not have a profile image.");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(student.getPhotoUrl()))
                .build();
    }

}
