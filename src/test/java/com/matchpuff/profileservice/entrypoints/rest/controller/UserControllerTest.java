package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.application.service.UserServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServicePort userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse mockUserResponse;
    private UserResponseInfo mockUserResponseInfo;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        mockUserResponse = UserResponse.builder()
                .id("user-1")
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .userType("STUDENT")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        mockUserResponseInfo = UserResponseInfo.builder()
                .id("user-1")
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .userType("STUDENT")
                .gender("MALE")
                .build();
    }

    private String buildValidUserRequestJson() {
        return """
                {
                  "name": "Test User",
                  "email": "test@escuelaing.edu.co",
                  "gender": "MALE",
                  "carreer": "SYSTEMS_ENGINEERING",
                  "semester": 5,
                  "photo": "http://photo.jpg",
                  "biography": "Una biografía de prueba",
                  "privacyLevel": "PUBLIC",
                  "tags": [
                    { "name": "Java", "category": "Programación" }
                  ],
                  "schedules": [
                    {
                      "dayOfWeek": "MONDAY",
                      "name": "Cálculo I",
                      "startTime": "08:00:00",
                      "endTime": "00:00:00"
                    }
                  ],
                  "dateOfBirth": "2000-01-01"
                }
                """;
    }

    // ── POST /api/users ───────────────────────────────────────────

    @Test
    void givenValidRequest_whenCreateUser_thenReturns201() throws Exception {
        when(userService.createUser(any())).thenReturn(mockUserResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildValidUserRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-1"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.userType").value("STUDENT"));
    }

    @Test
    void givenMissingRequiredFields_whenCreateUser_thenReturns400() throws Exception {
        String incompleteJson = """
                {
                  "name": "X"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEmptyTagsList_whenCreateUser_thenReturns400() throws Exception {
        String jsonWithEmptyTags = """
                {
                  "name": "Test User",
                  "email": "test@escuelaing.edu.co",
                  "gender": "MALE",
                  "carreer": "SYSTEMS_ENGINEERING",
                  "semester": 5,
                  "photo": "http://photo.jpg",
                  "privacyLevel": "PUBLIC",
                  "tags": [],
                  "schedules": [
                    {
                      "dayOfWeek": "MONDAY",
                      "name": "Clase",
                      "startTime": "08:00:00",
                      "endTime": "00:00:00"
                    }
                  ],
                  "dateOfBirth": "2000-01-01"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithEmptyTags))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/users/{userId} ───────────────────────────────────

    @Test
    void givenExistingId_whenGetUser_thenReturns200() throws Exception {
        when(userService.getUser("user-1")).thenReturn(mockUserResponseInfo);

        mockMvc.perform(get("/api/users/user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    // ── PATCH /api/users/{userId} ─────────────────────────────────

    @Test
    void givenValidRequest_whenUpdateUser_thenReturns200() throws Exception {
        when(userService.updateUser(eq("user-1"), any())).thenReturn(mockUserResponseInfo);

        mockMvc.perform(patch("/api/users/user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildValidUserRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    // ── GET /api/users ────────────────────────────────────────────

    @Test
    void whenGetAllUsers_thenReturns200WithList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(mockUserResponseInfo));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("user-1"));
    }

    @Test
    void whenGetAllUsersReturnsEmpty_thenReturns200WithEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── PATCH /api/users/{userId}/schedule ────────────────────────

    @Test
    void givenValidSchedule_whenAddSchedule_thenReturns200() throws Exception {
        when(userService.addSchedule(eq("user-1"), any())).thenReturn(mockUserResponseInfo);

        String scheduleJson = """
                {
                  "dayOfWeek": "WEDNESDAY",
                  "name": "Programación Avanzada",
                  "startTime": "10:00:00",
                  "endTime": "00:00:00"
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    @Test
    void givenInvalidSchedule_whenAddSchedule_thenReturns400() throws Exception {
        String invalidScheduleJson = """
                {
                  "name": "Sin día"
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidScheduleJson))
                .andExpect(status().isBadRequest());
    }

    // ── PATCH /api/users/{userId}/tags ────────────────────────────

    @Test
    void givenValidTag_whenAddTag_thenReturns200() throws Exception {
        when(userService.addTag(eq("user-1"), any())).thenReturn(mockUserResponseInfo);

        String tagJson = """
                {
                  "name": "Kubernetes",
                  "category": "DevOps"
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    @Test
    void givenInvalidTag_whenAddTag_thenReturns400() throws Exception {
        String invalidTagJson = """
                {
                  "name": ""
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTagJson))
                .andExpect(status().isBadRequest());
    }
}
