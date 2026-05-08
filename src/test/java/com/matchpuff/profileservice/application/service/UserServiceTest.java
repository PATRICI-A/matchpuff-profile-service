package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
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
        doNothing().when(userUseCase).deleteUser("user-1");

        userService.deleteUser("user-1");

        verify(userUseCase).deleteUser("user-1");
    }

    // ── updateProfileImage / getAllStudentProfiles ───────────────────────

    @Test
    void whenUpdateProfileImage_thenReturnsProfilePhotoResponse() {
        byte[] file = "bytes".getBytes();
        when(userUseCase.updateProfileImage("user-1", file, "image/png")).thenReturn(student);

        com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto photoResponse =
                com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto.builder()
                        .id(student.getId())
                        .name("Laura Torres")
                        .email("laura@escuelaing.edu.co")
                        .profileImageUrl("http://photo.jpg")
                        .build();

        when(userMapper.toResponseProfilePhoto(student)).thenReturn(photoResponse);

        var result = userService.updateProfileImage("user-1", file, "image/png");

        assertNotNull(result);
        assertEquals("http://photo.jpg", result.getProfileImageUrl());
        verify(userUseCase).updateProfileImage("user-1", file, "image/png");
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
        when(userUseCase.getUser("user-1")).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.getUser("user-1");

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        verify(userUseCase).getUser("user-1");
    }

    // ── updateUser ────────────────────────────────────────────────

    @Test
    void givenUserIdAndStudent_whenUpdateUser_thenReturnsMappedResponseInfo() {
        when(userUseCase.updateUser("user-1", student)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.updateUser("user-1", student);

        assertNotNull(result);
        verify(userUseCase).updateUser("user-1", student);
    }

    @Test
    void givenPasswordChangeRequest_whenChangePassword_thenDelegatesToUseCase() {
        doNothing().when(userUseCase).changePassword("user-1", "CurrentPassword123", "NewPassword123");

        userService.changePassword("user-1", "CurrentPassword123", "NewPassword123");

        verify(userUseCase).changePassword("user-1", "CurrentPassword123", "NewPassword123");
    }

    // ── addSchedule ───────────────────────────────────────────────

    @Test
    void givenUserIdAndSchedule_whenAddSchedule_thenReturnsMappedResponseInfo() {
        Schedule schedule = new Schedule( DayOfWeekEnum.WEDNESDAY, "Algebra", LocalTime.of(12, 0), LocalTime.of(14, 0));

        when(userUseCase.addScheduleToStudent("user-1", schedule)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.addSchedule("user-1", schedule);

        assertNotNull(result);
        verify(userUseCase).addScheduleToStudent("user-1", schedule);
    }

    // ── addTag ────────────────────────────────────────────────────

    @Test
    void givenUserIdAndTag_whenAddTag_thenReturnsMappedResponseInfo() {
        Tag tag = new Tag("Python", "Programación");

        when(userUseCase.addTagToStudent("user-1", tag)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.addTag("user-1", tag);

        assertNotNull(result);
        verify(userUseCase).addTagToStudent("user-1", tag);
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
}
