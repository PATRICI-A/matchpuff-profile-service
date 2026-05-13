package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternalUserServiceTest {

    @Mock
    private UserUseCasePort userUseCase;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private InternalUserService internalUserService;

    // ── getUser ───────────────────────────────────────────────────

    @Test
    void givenStudentId_whenGetUser_thenReturnsMappedAuthResponse() {
        UUID userId = UUID.randomUUID();
        StudentProfile student = new StudentProfile();
        student.setId(userId);

        UserAuthResponse response = UserAuthResponse.builder()
                .id(student.getId())
                .email("carlos@escuelaing.edu.co")
                .userType("STUDENT")
                .build();

        when(userUseCase.getUser(userId)).thenReturn(student);
        when(userMapper.toAuthResponse(student)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUser(userId);

        assertNotNull(result);
        assertEquals("STUDENT", result.getUserType());
        verify(userUseCase).getUser(userId);
        verify(userMapper).toAuthResponse(student);
    }

    @Test
    void givenAdminId_whenGetUser_thenReturnsMappedAuthResponse() {
        UUID adminId = UUID.randomUUID();
        Admin admin = new Admin();
        admin.setId(adminId);

        UserAuthResponse response = UserAuthResponse.builder()
                .id(admin.getId())
                .email("admin@escuelaing.edu.co")
                .userType("ADMIN")
                .build();

        when(userUseCase.getUser(adminId)).thenReturn(admin);
        when(userMapper.toAuthResponse(admin)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUser(adminId);

        assertNotNull(result);
        assertEquals("ADMIN", result.getUserType());
        verify(userUseCase).getUser(adminId);
    }

    // ── getUserByEmail ────────────────────────────────────────────

    @Test
    void givenStudentEmail_whenGetUserByEmail_thenReturnsMappedAuthResponse() {
        StudentProfile student = new StudentProfile();
        student.setId(UUID.randomUUID());

        UserAuthResponse response = UserAuthResponse.builder()
                .id(student.getId())
                .email("student@escuelaing.edu.co")
                .userType("STUDENT")
                .build();

        when(userUseCase.getUserByEmail("student@escuelaing.edu.co")).thenReturn(student);
        when(userMapper.toAuthResponse(student)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUserByEmail("student@escuelaing.edu.co");

        assertNotNull(result);
        assertEquals("STUDENT", result.getUserType());
        verify(userUseCase).getUserByEmail("student@escuelaing.edu.co");
    }

    @Test
    void givenOrganizerEmail_whenGetUserByEmail_thenReturnsMappedAuthResponse() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());

        UserAuthResponse response = UserAuthResponse.builder()
                .id(organizer.getId())
                .email("org@escuelaing.edu.co")
                .userType("ORGANIZER")
                .build();

        when(userUseCase.getUserByEmail("org@escuelaing.edu.co")).thenReturn(organizer);
        when(userMapper.toAuthResponse(organizer)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUserByEmail("org@escuelaing.edu.co");

        assertNotNull(result);
        assertEquals("ORGANIZER", result.getUserType());
    }

    // ── verifyUser ────────────────────────────────────────────────

    @Test
    void givenUserId_whenVerifyUser_thenDelegatesToUseCase() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userUseCase).verifyUser(userId);

        internalUserService.verifyUser(userId);

        verify(userUseCase).verifyUser(userId);
    }

    @Test
    void givenDifferentUserId_whenVerifyUser_thenDelegatesToUseCaseWithCorrectId() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userUseCase).verifyUser(userId);

        internalUserService.verifyUser(userId);

        verify(userUseCase).verifyUser(userId);
    }
}
