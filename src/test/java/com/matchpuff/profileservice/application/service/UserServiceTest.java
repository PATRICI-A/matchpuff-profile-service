package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
// tags are represented by UUIDs now
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserUseCasePort userUseCase;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private StudentProfile student;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        student = new StudentProfile();
        student.setId(UUID.randomUUID());
        student.setName("Laura Torres");
        student.setEmail("laura@escuelaing.edu.co");

        userResponse = UserResponse.builder()
                .id(student.getId())
                .name("Laura Torres")
                .email("laura@escuelaing.edu.co")
                .userType("STUDENT")
                .createdAt(LocalDateTime.now())
                .build();


    }

    // ── createAdmin / createOrganizer / deleteUser ─────────────────────────

    @Test
    void givenAdmin_whenCreateAdminUser_thenDelegatesAndReturnsMappedResponse() {
        var admin = new com.matchpuff.profileservice.domain.model.Admin();
        admin.setId(UUID.randomUUID());
        admin.setName("Admin User");

        when(userUseCase.createAdminUser(admin)).thenReturn(admin);
        when(userMapper.toResponse(admin)).thenReturn(userResponse);

        UserResponse result = userService.createAdminUser(admin);

        assertNotNull(result);
        verify(userUseCase).createAdminUser(admin);
        verify(userMapper).toResponse(admin);
    }

    @Test
    void givenOrganizer_whenCreateOrganizerUser_thenDelegatesAndReturnsMappedResponse() {
        var organizer = new com.matchpuff.profileservice.domain.model.Organizer();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Organizer User");

        when(userUseCase.createOrganizerUser(organizer)).thenReturn(organizer);
        when(userMapper.toResponse(organizer)).thenReturn(userResponse);

        UserResponse result = userService.createOrganizerUser(organizer);

        assertNotNull(result);
        verify(userUseCase).createOrganizerUser(organizer);
        verify(userMapper).toResponse(organizer);
    }

    @Test
    void whenDeleteUser_thenDelegatesToUseCase() {
        doNothing().when(userUseCase).deleteUser(student.getId());

        userService.deleteUser(student.getId());

        verify(userUseCase).deleteUser(student.getId());
    }

    // ── updateProfileImage / getAllStudentProfiles ───────────────────────

    @Test
    void whenUpdateProfileImage_thenReturnsProfilePhotoResponse() {
        byte[] file = "bytes".getBytes();
        when(userUseCase.updateProfileImage(student.getId(), file, "image/png")).thenReturn(student);

        com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto photoResponse =
                com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto.builder()
                        .id(student.getId())
                        .name("Laura Torres")
                        .email("laura@escuelaing.edu.co")
                        .profileImageUrl("http://photo.jpg")
                        .build();

        when(userMapper.toResponseProfilePhoto(student)).thenReturn(photoResponse);

        var result = userService.updateProfileImage(student.getId(), file, "image/png");

        assertNotNull(result);
        assertEquals("http://photo.jpg", result.getProfileImageUrl());
        verify(userUseCase).updateProfileImage(student.getId(), file, "image/png");
        verify(userMapper).toResponseProfilePhoto(student);
    }

    @Test
    void whenGetAllStudentProfiles_thenReturnsMappedList() {
        StudentProfile s = new StudentProfile();
        s.setId(UUID.randomUUID());
        s.setName("Student One");

        when(userUseCase.getAllStudentProfiles()).thenReturn(List.of(s));
        when(userMapper.toResponse(s)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllStudentProfiles();

        assertEquals(1, result.size());
        verify(userUseCase).getAllStudentProfiles();
    }

    // ── createUser ────────────────────────────────────────────────

    @Test
    void givenValidStudent_whenCreateUser_thenReturnsMappedResponse() {
        when(userUseCase.createStudentUser(student)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.createStudentUser(student);

        assertNotNull(result);
        assertEquals("Laura Torres", result.getName());
        verify(userUseCase).createStudentUser(student);
        verify(userMapper).toResponse(student);
    }

    // ── getUser ───────────────────────────────────────────────────

    @Test
    void givenUserId_whenGetUser_thenReturnsMappedResponseInfo() {
        when(userUseCase.getUser(student.getId())).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.getUser(student.getId());

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        verify(userUseCase).getUser(student.getId());
    }

    // ── updateUser ────────────────────────────────────────────────

    @Test
    void givenUserIdAndStudent_whenUpdateUser_thenReturnsMappedResponseInfo() {
        when(userUseCase.updateUser(student.getId(), student)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(student.getId(), student);

        assertNotNull(result);
        verify(userUseCase).updateUser(student.getId(), student);
    }

    @Test
    void givenPasswordChangeRequest_whenChangePassword_thenDelegatesToUseCase() {
        doNothing().when(userUseCase).changePassword(student.getId(), "CurrentPassword123", "NewPassword123");

        userService.changePassword(student.getId(), "CurrentPassword123", "NewPassword123");

        verify(userUseCase).changePassword(student.getId(), "CurrentPassword123", "NewPassword123");
    }

    // ── addSchedule ───────────────────────────────────────────────

    @Test
    void givenUserIdAndSchedule_whenAddSchedule_thenReturnsMappedResponseInfo() {
        Schedule schedule = new Schedule( DayOfWeekEnum.WEDNESDAY, "Algebra", LocalTime.of(12, 0), LocalTime.of(14, 0));

        when(userUseCase.addScheduleToStudent(student.getId(), schedule)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.addSchedule(student.getId(), schedule);

        assertNotNull(result);
        verify(userUseCase).addScheduleToStudent(student.getId(), schedule);
    }

    // ── addTag ────────────────────────────────────────────────────

    @Test
    void givenUserIdAndTag_whenAddTag_thenReturnsMappedResponseInfo() {
        java.util.UUID tagId = UUID.randomUUID();

        when(userUseCase.addTagToStudent(student.getId(), tagId)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.addTag(student.getId(), tagId);

        assertNotNull(result);
        verify(userUseCase).addTagToStudent(student.getId(), tagId);
    }

    @Test
    void givenUserIdAndSchedule_whenRemoveSchedule_thenReturnsMappedResponseInfo() {
        Schedule schedule = new Schedule(DayOfWeekEnum.WEDNESDAY, "Algebra", LocalTime.of(12, 0), LocalTime.of(14, 0));

        when(userUseCase.removeScheduleFromStudent(student.getId(), schedule)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.removeSchedule(student.getId(), schedule);

        assertNotNull(result);
        verify(userUseCase).removeScheduleFromStudent(student.getId(), schedule);
    }

    @Test
    void givenUserIdAndTag_whenRemoveTag_thenReturnsMappedResponseInfo() {
        java.util.UUID tagId2 = UUID.randomUUID();

        when(userUseCase.removeTagFromStudent(student.getId(), tagId2)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.removeTag(student.getId(), tagId2);

        assertNotNull(result);
        verify(userUseCase).removeTagFromStudent(student.getId(), tagId2);
    }

    // ── getAllUsers ────────────────────────────────────────────────

    @Test
    void whenGetAllUsers_thenReturnsMappedList() {
        when(userUseCase.getAllUsers()).thenReturn(List.of(student));
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userUseCase).getAllUsers();
    }

    @Test
    void whenGetAllUsersReturnsEmpty_thenResultIsEmpty() {
        when(userUseCase.getAllUsers()).thenReturn(List.of());

        List<UserResponse> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    // ── getUserByEmail ────────────────────────────────────────────

    @Test
    void givenEmail_whenGetUserByEmail_thenReturnsMappedResponse() {
        when(userUseCase.getUserByEmail("laura@escuelaing.edu.co")).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.getUserByEmail("laura@escuelaing.edu.co");

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        verify(userUseCase).getUserByEmail("laura@escuelaing.edu.co");
    }

    // ── updateGeolocation ─────────────────────────────────────────

    @Test
    void givenUserIdAndGeolocationTrue_whenUpdateGeolocation_thenReturnsMappedResponse() {
        when(userUseCase.updateGeolocation(student.getId(), true)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.updateGeolocation(student.getId(), true);

        assertNotNull(result);
        verify(userUseCase).updateGeolocation(student.getId(), true);
        verify(userMapper).toResponse(student);
    }

    @Test
    void givenUserIdAndGeolocationFalse_whenUpdateGeolocation_thenReturnsMappedResponse() {
        when(userUseCase.updateGeolocation(student.getId(), false)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.updateGeolocation(student.getId(), false);

        assertNotNull(result);
        verify(userUseCase).updateGeolocation(student.getId(), false);
    }
}
