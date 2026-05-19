package com.matchpuff.profileservice.entrypoints.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.matchpuff.profileservice.application.dto.response.BatchProfileResponse;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.domain.model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = {
        UserCreationController.class,
        UserQueryController.class,
        UserUpdateController.class,
        UserMediaController.class,
        UserDeletionController.class
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServicePort userService;

    @MockitoBean
    private UserMapper userRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse mockUserResponse;
    private UserResponse mockUserResponseInfo;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        mockUserResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .userType("STUDENT")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        mockUserResponseInfo = UserResponse.builder()
                .id(UUID.randomUUID())
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
                  "email": "test@mail.escuelaing.edu.co",
                  "password": "TestPassword123",
                  "gender": "MALE",
                  "career": "SYSTEMS_ENGINEERING",
                  "semester": 5,
                  "studentCarnet": 2021123411,
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
                  "geolocationEnabled": true,
                  "dateOfBirth": "2000-01-01"
                }
                """;
    }

    // ── POST /api/users ───────────────────────────────────────────

    @Test
    @WithMockUser
    void givenValidRequest_whenCreateUser_thenReturns201() throws Exception {
        when(userService.createStudentUser(any())).thenReturn(mockUserResponse);

        mockMvc.perform(post("/api/v1/users/student")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildValidUserRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUserResponse.getId().toString()))
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

        mockMvc.perform(post("/api/v1/users/student")
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
                  "career": "SYSTEMS_ENGINEERING",
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

        mockMvc.perform(post("/api/v1/users/student")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithEmptyTags))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/v1/users/{userId} ───────────────────────────────────

    @Test
    @WithMockUser
    void givenExistingId_whenGetUser_thenReturns200() throws Exception {
        when(userService.getUser(mockUserResponseInfo.getId())).thenReturn(mockUserResponseInfo);

        mockMvc.perform(get("/api/v1/users/" + mockUserResponseInfo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    // ── PATCH /api/v1/users/{userId} ─────────────────────────────────

    @Test
    @WithMockUser
    void givenValidRequest_whenUpdateUser_thenReturns200() throws Exception {
        when(userService.updateUser(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        mockMvc.perform(patch("/api/v1/users/student/" + mockUserResponseInfo.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildValidUserRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
    void givenValidPasswordChangeRequest_whenChangePassword_thenReturns204() throws Exception {
        doNothing().when(userService).changePassword(mockUserResponseInfo.getId(), "CurrentPassword123", "NewPassword123");

        String passwordJson = """
                {
                  "currentPassword": "CurrentPassword123",
                  "newPassword": "NewPassword123"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passwordJson))
                .andExpect(status().isNoContent());
    }

    // ── GET /api/v1/users ────────────────────────────────────────────

    @Test
    @WithMockUser
    void whenGetAllUsers_thenReturns200WithList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(mockUserResponseInfo));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
    void givenValidBatchRequest_whenGetUsersByIds_thenReturns200WithProfiles() throws Exception {
        UUID userId = mockUserResponseInfo.getId();
        when(userService.getUsersByIds(List.of(userId))).thenReturn(List.of(
                new BatchProfileResponse(userId, "Test User", "test@escuelaing.edu.co", "Bio", "http://photo.jpg")
        ));

        String batchJson = String.format("""
                {
                  "ids": ["%s"]
                }
                """, userId);

        mockMvc.perform(post("/api/v1/users/batch")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(batchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    @WithMockUser
    void whenGetAllUsersReturnsEmpty_thenReturns200WithEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── PATCH /api/v1/users/{userId}/schedule ────────────────────────

    @Test
    @WithMockUser
    void givenValidSchedule_whenAddSchedule_thenReturns200() throws Exception {
        when(userService.addSchedule(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        String scheduleJson = """
                {
                  "dayOfWeek": "WEDNESDAY",
                  "name": "Programación Avanzada",
                  "startTime": "10:00:00",
                  "endTime": "12:00:00"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/schedule")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
    void givenInvalidSchedule_whenAddSchedule_thenReturns400() throws Exception {
        String invalidScheduleJson = """
                {
                  "name": "Sin día"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/schedule")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidScheduleJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void givenValidSchedule_whenRemoveSchedule_thenReturns200() throws Exception {
        when(userService.removeSchedule(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        String scheduleJson = """
                {
                  "dayOfWeek": "WEDNESDAY",
                  "name": "Programación Avanzada",
                  "startTime": "10:00:00",
                  "endTime": "12:00:00"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/schedule/remove")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    // ── PATCH /api/v1/users/{userId}/tags ────────────────────────────

    @Test
    @WithMockUser
    void givenValidTag_whenAddTag_thenReturns200() throws Exception {
        when(userService.addTag(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        String tagId = UUID.randomUUID().toString();
        String tagJson = String.format("{ \"tagId\": \"%s\" }", tagId);

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
    void givenInvalidTag_whenAddTag_thenReturns400() throws Exception {
        String invalidTagJson = """
                {
                  "name": ""
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/tags")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidTagJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void givenValidTag_whenRemoveTag_thenReturns200() throws Exception {
        when(userService.removeTag(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        String tagId2 = UUID.randomUUID().toString();
        String tagJson2 = String.format("{ \"tagId\": \"%s\" }", tagId2);

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/tags/remove")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    // ── POST /api/v1/users/admin ─────────────────────────────────────

    @Test
    @WithMockUser
    void givenValidAdminRequest_whenCreateAdmin_thenReturns201() throws Exception {
        when(userService.createAdminUser(any())).thenReturn(mockUserResponse);

                                String adminJson = """
                                                                {
                                                                        "name": "Admin User",
                                                                        "email": "admin@escuelaing.edu.co",
                                                                        "password": "AdminPass123",
                                                                        "gender": "MALE"
                                                                }
                                                                """;

        mockMvc.perform(post("/api/v1/users/admin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUserResponse.getId().toString()));
    }

    // ── POST /api/v1/users/organizer ─────────────────────────────────

    @Test
    @WithMockUser
    void givenValidOrganizerRequest_whenCreateOrganizer_thenReturns201() throws Exception {
        when(userService.createOrganizerUser(any())).thenReturn(mockUserResponse);

                                String orgJson = """
                                                                {
                                                                        "name": "Org User",
                                                                        "email": "org@escuelaing.edu.co",
                                                                        "password": "OrgPass123",
                                                                        "gender": "MALE",
                                                                        "contactInfo": "contact"
                                                                }
                                                                """;

        mockMvc.perform(post("/api/v1/users/organizer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orgJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUserResponse.getId().toString()));
    }

    // ── GET /api/v1/users/student-profiles ───────────────────────────

    @Test
    @WithMockUser
    void whenGetAllStudentProfiles_thenReturns200WithList() throws Exception {
        when(userService.getAllStudentProfiles()).thenReturn(List.of(mockUserResponseInfo));

        mockMvc.perform(get("/api/v1/users/student-profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(mockUserResponseInfo.getId().toString()));
    }

    // ── DELETE /api/v1/users/{userId} ─────────────────────────────────

    @Test
    @WithMockUser
   void whenDeleteUser_thenReturns204() throws Exception {
           doNothing().when(userService).deleteUser(mockUserResponseInfo.getId());

        mockMvc.perform(delete("/api/v1/users/" + mockUserResponseInfo.getId())
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    // ── POST /api/v1/users/{userId}/profile-image ─────────────────────

    @Test
    @WithMockUser
    void whenUploadProfileImage_thenReturns200WithPhotoInfo() throws Exception {
        byte[] content = "hello".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", content);

        com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto photo =
                com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto.builder()
                        .id(mockUserResponse.getId())
                        .name("Test User")
                        .email("test@escuelaing.edu.co")
                        .profileImageUrl("http://photo.jpg")
                        .build();

        when(userService.updateProfileImage(eq(mockUserResponse.getId()), any(byte[].class), eq("image/png")))
                .thenReturn(photo);

        mockMvc.perform(multipart("/api/v1/users/" + mockUserResponse.getId() + "/profile-image")
                .file(file)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileImageUrl").value("http://photo.jpg"));
    }

    @Test
    @WithMockUser
    void givenStudentWithPhoto_whenGetProfileImage_thenRedirectsToImageUrl() throws Exception {
        StudentProfileResponse photoUser = StudentProfileResponse.builder()
                .id(mockUserResponse.getId())
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .photoUrl("http://photo.jpg")
                .build();

        when(userService.getUser(mockUserResponse.getId())).thenReturn(photoUser);

        mockMvc.perform(get("/api/v1/users/" + mockUserResponse.getId() + "/profile-image"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "http://photo.jpg"));
    }

    @Test
    @WithMockUser
    void givenStudentWithoutPhoto_whenGetProfileImage_thenReturns400() throws Exception {
        StudentProfileResponse photoUser = StudentProfileResponse.builder()
                .id(mockUserResponse.getId())
                .name("Test User")
                .email("test@escuelaing.edu.co")
                .build();

        when(userService.getUser(mockUserResponse.getId())).thenReturn(photoUser);

        mockMvc.perform(get("/api/v1/users/" + mockUserResponse.getId() + "/profile-image"))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/v1/users/mail/{email} ────────────────────────────

    @Test
    @WithMockUser
    void givenExistingEmail_whenGetUserByEmail_thenReturns200() throws Exception {
        when(userService.getUserByEmail("test@escuelaing.edu.co")).thenReturn(mockUserResponseInfo);

        mockMvc.perform(get("/api/v1/users/mail/test@escuelaing.edu.co"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    // ── PATCH /api/v1/users/admin/{userId} ───────────────────────

    @Test
    @WithMockUser
    void givenValidAdminUpdateRequest_whenUpdateUserAdmin_thenReturns200() throws Exception {
        when(userRestMapper.toDomain(any(UserAdminUpdateRequest.class))).thenReturn(new Admin());
        when(userService.updateUser(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        String adminUpdateJson = """
                {
                  "name": "Updated Admin",
                  "email": "admin@escuelaing.edu.co",
                  "gender": "MALE"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/admin/" + mockUserResponseInfo.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
    void givenMissingGender_whenUpdateUserAdmin_thenReturns400() throws Exception {
        String invalidAdminJson = """
                {
                  "name": "Updated Admin"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/admin/" + mockUserResponseInfo.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAdminJson))
                .andExpect(status().isBadRequest());
    }

    // ── PATCH /api/v1/users/organizer/{userId} ────────────────────

    @Test
    @WithMockUser
         void givenValidOrganizerUpdateRequest_whenUpdateUserOrganizer_thenReturns200() throws Exception {
                         when(userService.updateUser(eq(mockUserResponseInfo.getId()), any())).thenReturn(mockUserResponseInfo);

        String orgUpdateJson = """
                {
                  "name": "Updated Org",
                  "email": "org@escuelaing.edu.co",
                  "gender": "FEMALE",
                  "contactInfo": "new@contact.co"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/organizer/" + mockUserResponseInfo.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orgUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
    void givenMissingContactInfo_whenUpdateUserOrganizer_thenReturns400() throws Exception {
        String invalidOrgJson = """
                {
                  "name": "Updated Org",
                  "gender": "FEMALE"
                }
                """;

        mockMvc.perform(patch("/api/v1/users/organizer/" + mockUserResponseInfo.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrgJson))
                .andExpect(status().isBadRequest());
    }

    // ── PATCH /api/v1/users/{userId}/geolocation ──────────────────

    @Test
    @WithMockUser
         void givenGeolocationEnabledTrue_whenUpdateGeolocation_thenReturns200() throws Exception {
                         when(userService.updateGeolocation(eq(mockUserResponseInfo.getId()), eq(true))).thenReturn(mockUserResponseInfo);

        String geoJson = """
                {
                  "geolocationEnabled": true
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/geolocation")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(geoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserResponseInfo.getId().toString()));
    }

    @Test
    @WithMockUser
         void givenGeolocationEnabledFalse_whenUpdateGeolocation_thenReturns200() throws Exception {
                         when(userService.updateGeolocation(eq(mockUserResponseInfo.getId()), eq(false))).thenReturn(mockUserResponseInfo);

        String geoJson = """
                {
                  "geolocationEnabled": false
                }
                """;

        mockMvc.perform(patch("/api/v1/users/" + mockUserResponseInfo.getId() + "/geolocation")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(geoJson))
                .andExpect(status().isOk());
    }
}
