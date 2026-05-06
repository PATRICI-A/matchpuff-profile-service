package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServicePort userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse mockUserResponse;
    private UserResponse mockUserResponseInfo;

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

        mockUserResponseInfo = UserResponse.builder()
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
                  "password": "TestPassword123",
                  "gender": "MALE",
                  "carreer": "SYSTEMS_ENGINEERING",
                  "semester": 5,
                  "studentCarnet": 20211234,
                  "photourl": "http://photo.jpg",
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
                      "endTime": "10:00:00"
                    }
                  ],
                  "dateOfBirth": "2000-01-01"
                }
                """;
    }

    // ── POST /api/users ───────────────────────────────────────────

    @Test
    @WithMockUser
    void givenValidRequest_whenCreateUser_thenReturns201() throws Exception {
        when(userService.createStudentUser(any())).thenReturn(mockUserResponse);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildValidUserRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-1"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.userType").value("STUDENT"));
    }

    @Test
    @WithMockUser
    void givenMissingRequiredFields_whenCreateUser_thenReturns400() throws Exception {
        String incompleteJson = """
                {
                  "name": "X"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void givenEmptyTagsList_whenCreateUser_thenReturns400() throws Exception {
        String jsonWithEmptyTags = """
                {
                  "name": "Test User",
                  "email": "test@escuelaing.edu.co",
                  "gender": "MALE",
                  "carreer": "SYSTEMS_ENGINEERING",
                  "semester": 5,
                  "photourl": "http://photo.jpg",
                  "privacyLevel": "PUBLIC",
                  "tags": [],
                  "schedules": [
                    {
                      "dayOfWeek": "MONDAY",
                      "name": "Clase",
                      "startTime": "08:00:00",
                      "endTime": "10:00:00"
                    }
                  ],
                  "dateOfBirth": "2000-01-01"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithEmptyTags))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/users/{userId} ───────────────────────────────────

    @Test
    @WithMockUser
    void givenExistingId_whenGetUser_thenReturns200() throws Exception {
        when(userService.getUser("user-1")).thenReturn(mockUserResponseInfo);

        mockMvc.perform(get("/api/users/user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    // ── PATCH /api/users/{userId} ─────────────────────────────────

    @Test
    @WithMockUser
    void givenValidRequest_whenUpdateUser_thenReturns200() throws Exception {
        when(userService.updateUser(eq("user-1"), any())).thenReturn(mockUserResponseInfo);

        mockMvc.perform(patch("/api/users/user-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildValidUserRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    // ── GET /api/users ────────────────────────────────────────────

    @Test
    @WithMockUser
    void whenGetAllUsers_thenReturns200WithList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(mockUserResponseInfo));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("user-1"));
    }

    @Test
    @WithMockUser
    void whenGetAllUsersReturnsEmpty_thenReturns200WithEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── PATCH /api/users/{userId}/schedule ────────────────────────

    @Test
    @WithMockUser
    void givenValidSchedule_whenAddSchedule_thenReturns200() throws Exception {
        when(userService.addSchedule(eq("user-1"), any())).thenReturn(mockUserResponseInfo);

        String scheduleJson = """
                {
                  "dayOfWeek": "WEDNESDAY",
                  "name": "Programación Avanzada",
                  "startTime": "10:00:00",
                  "endTime": "12:00:00"
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/schedule")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    @Test
    @WithMockUser
    void givenInvalidSchedule_whenAddSchedule_thenReturns400() throws Exception {
        String invalidScheduleJson = """
                {
                  "name": "Sin día"
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/schedule")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidScheduleJson))
                .andExpect(status().isBadRequest());
    }

    // ── PATCH /api/users/{userId}/tags ────────────────────────────

    @Test
    @WithMockUser
    void givenValidTag_whenAddTag_thenReturns200() throws Exception {
        when(userService.addTag(eq("user-1"), any())).thenReturn(mockUserResponseInfo);

        String tagJson = """
                {
                  "name": "Kubernetes",
                  "category": "DevOps"
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    @Test
    @WithMockUser
    void givenInvalidTag_whenAddTag_thenReturns400() throws Exception {
        String invalidTagJson = """
                {
                  "name": ""
                }
                """;

        mockMvc.perform(patch("/api/users/user-1/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTagJson))
                .andExpect(status().isBadRequest());
    }

    // ── POST /api/users/admin ─────────────────────────────────────

    @Test
    @WithMockUser
    void givenValidAdminRequest_whenCreateAdmin_thenReturns201() throws Exception {
        when(userService.createAdminUser(any())).thenReturn(mockUserResponse);

                                String adminJson = """
                                                                {
                                                                        "name": "Admin User",
                                                                        "email": "admin@escuelaing.edu.co",
                                                                        "gender": "MALE"
                                                                }
                                                                """;

        mockMvc.perform(post("/api/users/admin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    // ── POST /api/users/organizer ─────────────────────────────────

    @Test
    @WithMockUser
    void givenValidOrganizerRequest_whenCreateOrganizer_thenReturns201() throws Exception {
        when(userService.createOrganizerUser(any())).thenReturn(mockUserResponse);

                                String orgJson = """
                                                                {
                                                                        "name": "Org User",
                                                                        "email": "org@escuelaing.edu.co",
                                                                        "gender": "MALE",
                                                                        "contactInfo": "contact"
                                                                }
                                                                """;

        mockMvc.perform(post("/api/users/organizer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orgJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-1"));
    }

    // ── GET /api/users/student-profiles ───────────────────────────

    @Test
    @WithMockUser
    void whenGetAllStudentProfiles_thenReturns200WithList() throws Exception {
        when(userService.getAllStudentProfiles()).thenReturn(List.of(mockUserResponseInfo));

        mockMvc.perform(get("/api/users/student-profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("user-1"));
    }

    // ── DELETE /api/users/{userId} ─────────────────────────────────

    @Test
    @WithMockUser
    void whenDeleteUser_thenReturns204() throws Exception {
        doNothing().when(userService).deleteUser("user-1");

        mockMvc.perform(delete("/api/users/user-1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // ── POST /api/users/{userId}/profile-image ─────────────────────

    @Test
    @WithMockUser
    void whenUploadProfileImage_thenReturns200WithPhotoInfo() throws Exception {
        byte[] content = "hello".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", content);

        com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto photo =
                com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto.builder()
                        .id("user-1")
                        .name("Test User")
                        .email("test@escuelaing.edu.co")
                        .profileImageUrl("http://photo.jpg")
                        .build();

        when(userService.updateProfileImage(eq("user-1"), any(byte[].class), eq("image/png")))
                .thenReturn(photo);

        mockMvc.perform(multipart("/api/users/user-1/profile-image")
                .file(file)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileImageUrl").value("http://photo.jpg"));
    }

    @Test
    @WithMockUser
    void givenStudentWithPhoto_whenGetProfileImage_thenRedirectsToImageUrl() throws Exception {
        StudentProfileResponse photoUser = StudentProfileResponse.builder()
                .id("user-1")
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .photoUrl("http://photo.jpg")
                .build();

        when(userService.getUser("user-1")).thenReturn(photoUser);

        mockMvc.perform(get("/api/users/user-1/profile-image"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://photo.jpg"));
    }

    @Test
    @WithMockUser
    void givenStudentWithoutPhoto_whenGetProfileImage_thenReturns400() throws Exception {
        StudentProfileResponse photoUser = StudentProfileResponse.builder()
                .id("user-1")
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .build();

        when(userService.getUser("user-1")).thenReturn(photoUser);

        mockMvc.perform(get("/api/users/user-1/profile-image"))
                .andExpect(status().isBadRequest());
    }
}
