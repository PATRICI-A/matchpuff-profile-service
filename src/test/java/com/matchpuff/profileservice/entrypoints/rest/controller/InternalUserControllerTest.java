package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.service.InternalUserServicePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InternalUserController.class)
class InternalUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InternalUserServicePort internalUserService;

    // ── GET /api/v1/internal/users/{userId} ──────────────────────

    @Test
    @WithMockUser
    void givenExistingStudentId_whenGetUser_thenReturns200WithStudentType() throws Exception {
        UUID userId = UUID.randomUUID();
        UserAuthResponse response = UserAuthResponse.builder()
                .id(userId)
                .email("student@escuelaing.edu.co")
                .userType("STUDENT")
                .verified(false)
                .build();

        when(internalUserService.getUser(userId.toString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/internal/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userType").value("STUDENT"))
                .andExpect(jsonPath("$.email").value("student@escuelaing.edu.co"));
    }

    @Test
    @WithMockUser
    void givenExistingAdminId_whenGetUser_thenReturns200WithAdminType() throws Exception {
        UUID userId = UUID.randomUUID();
        UserAuthResponse response = UserAuthResponse.builder()
                .id(userId)
                .email("admin@escuelaing.edu.co")
                .userType("ADMIN")
                .verified(true)
                .build();

        when(internalUserService.getUser(userId.toString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/internal/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userType").value("ADMIN"))
                .andExpect(jsonPath("$.verified").value(true));
    }

    // ── GET /api/v1/internal/users/mail/{email} ──────────────────

    @Test
    @WithMockUser
    void givenExistingEmail_whenGetUserByEmail_thenReturns200() throws Exception {
        UUID userId = UUID.randomUUID();
        UserAuthResponse response = UserAuthResponse.builder()
                .id(userId)
                .email("org@escuelaing.edu.co")
                .userType("ORGANIZER")
                .build();

        when(internalUserService.getUserByEmail("org@escuelaing.edu.co")).thenReturn(response);

        mockMvc.perform(get("/api/v1/internal/users/mail/org@escuelaing.edu.co"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userType").value("ORGANIZER"))
                .andExpect(jsonPath("$.email").value("org@escuelaing.edu.co"));
    }

    @Test
    @WithMockUser
    void givenStudentEmail_whenGetUserByEmail_thenReturns200WithId() throws Exception {
        UUID userId = UUID.randomUUID();
        UserAuthResponse response = UserAuthResponse.builder()
                .id(userId)
                .email("user@escuelaing.edu.co")
                .userType("STUDENT")
                .build();

        when(internalUserService.getUserByEmail("user@escuelaing.edu.co")).thenReturn(response);

        mockMvc.perform(get("/api/v1/internal/users/mail/user@escuelaing.edu.co"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    // ── PATCH /api/v1/internal/users/{userId}/verify ─────────────

    @Test
    @WithMockUser
    void givenUserId_whenVerifyUser_thenReturns204() throws Exception {
        UUID userId = UUID.randomUUID();
        doNothing().when(internalUserService).verifyUser(userId.toString());

        mockMvc.perform(patch("/api/v1/internal/users/" + userId + "/verify")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void givenAnotherUserId_whenVerifyUser_thenReturns204() throws Exception {
        String userId = UUID.randomUUID().toString();
        doNothing().when(internalUserService).verifyUser(userId);

        mockMvc.perform(patch("/api/v1/internal/users/" + userId + "/verify")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
