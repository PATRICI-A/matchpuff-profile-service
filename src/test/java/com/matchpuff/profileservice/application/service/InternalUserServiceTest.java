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
        StudentProfile student = new StudentProfile();
        student.setId(UUID.randomUUID());

        UserAuthResponse response = UserAuthResponse.builder()
                .id(student.getId())
                .email("carlos@escuelaing.edu.co")
                .userType("STUDENT")
                .build();

        when(userUseCase.getUser("user-1")).thenReturn(student);
        when(userMapper.toAuthResponse(student)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUser("user-1");

        assertNotNull(result);
        assertEquals("STUDENT", result.getUserType());
        verify(userUseCase).getUser("user-1");
        verify(userMapper).toAuthResponse(student);
    }

    @Test
    void givenAdminId_whenGetUser_thenReturnsMappedAuthResponse() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());

        UserAuthResponse response = UserAuthResponse.builder()
                .id(admin.getId())
                .email("admin@escuelaing.edu.co")
                .userType("ADMIN")
                .build();

        when(userUseCase.getUser("admin-1")).thenReturn(admin);
        when(userMapper.toAuthResponse(admin)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUser("admin-1");

        assertNotNull(result);
        assertEquals("ADMIN", result.getUserType());
        verify(userUseCase).getUser("admin-1");
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
        doNothing().when(userUseCase).verifyUser("user-1");

        internalUserService.verifyUser("user-1");

        verify(userUseCase).verifyUser("user-1");
    }

    @Test
    void givenDifferentUserId_whenVerifyUser_thenDelegatesToUseCaseWithCorrectId() {
        String userId = UUID.randomUUID().toString();
        doNothing().when(userUseCase).verifyUser(userId);

        internalUserService.verifyUser(userId);

        verify(userUseCase).verifyUser(userId);
        verify(userUseCase, never()).verifyUser("user-1");
    }
}
