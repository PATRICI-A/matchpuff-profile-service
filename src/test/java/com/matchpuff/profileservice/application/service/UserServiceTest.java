package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

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
    private UserResponseInfo userResponseInfo;

    @BeforeEach
    void setUp() {
        student = new StudentProfile();
        student.setId("user-1");
        student.setName("Laura Torres");
        student.setEmail("laura@escuelaing.edu.co");

        userResponse = UserResponse.builder()
                .id("user-1")
                .name("Laura Torres")
                .email("laura@escuelaing.edu.co")
                .userType("STUDENT")
                .createdAt(LocalDateTime.now())
                .build();

        userResponseInfo = UserResponseInfo.builder()
                .id("user-1")
                .name("Laura Torres")
                .email("laura@escuelaing.edu.co")
                .userType("STUDENT")
                .build();
    }

    // ── createUser ────────────────────────────────────────────────

    @Test
    void givenValidStudent_whenCreateUser_thenReturnsMappedResponse() {
        when(userUseCase.createStudentUser(student)).thenReturn(student);
        when(userMapper.toResponse(student)).thenReturn(userResponse);

        UserResponse result = userService.createUser(student);

        assertNotNull(result);
        assertEquals("Laura Torres", result.getName());
        verify(userUseCase).createStudentUser(student);
        verify(userMapper).toResponse(student);
    }

    // ── getUser ───────────────────────────────────────────────────

    @Test
    void givenUserId_whenGetUser_thenReturnsMappedResponseInfo() {
        when(userUseCase.getUser("user-1")).thenReturn(student);
        when(userMapper.toResponseInfo(student)).thenReturn(userResponseInfo);

        UserResponseInfo result = userService.getUser("user-1");

        assertNotNull(result);
        assertEquals("user-1", result.getId());
        verify(userUseCase).getUser("user-1");
    }

    // ── updateUser ────────────────────────────────────────────────

    @Test
    void givenUserIdAndStudent_whenUpdateUser_thenReturnsMappedResponseInfo() {
        when(userUseCase.updateStudentUser("user-1", student)).thenReturn(student);
        when(userMapper.toResponseInfo(student)).thenReturn(userResponseInfo);

        UserResponseInfo result = userService.updateUser("user-1", student);

        assertNotNull(result);
        verify(userUseCase).updateStudentUser("user-1", student);
    }

    // ── addSchedule ───────────────────────────────────────────────

    @Test
    void givenUserIdAndSchedule_whenAddSchedule_thenReturnsMappedResponseInfo() {
        Schedule schedule = new Schedule();
        schedule.setName("Algebra");

        when(userUseCase.addScheduleToStudent("user-1", schedule)).thenReturn(student);
        when(userMapper.toResponseInfo(student)).thenReturn(userResponseInfo);

        UserResponseInfo result = userService.addSchedule("user-1", schedule);

        assertNotNull(result);
        verify(userUseCase).addScheduleToStudent("user-1", schedule);
    }

    // ── addTag ────────────────────────────────────────────────────

    @Test
    void givenUserIdAndTag_whenAddTag_thenReturnsMappedResponseInfo() {
        Tag tag = new Tag();
        tag.setName("Python");
        tag.setCategory("Programación");

        when(userUseCase.addTagToStudent("user-1", tag)).thenReturn(student);
        when(userMapper.toResponseInfo(student)).thenReturn(userResponseInfo);

        UserResponseInfo result = userService.addTag("user-1", tag);

        assertNotNull(result);
        verify(userUseCase).addTagToStudent("user-1", tag);
    }

    // ── getAllUsers ────────────────────────────────────────────────

    @Test
    void whenGetAllUsers_thenReturnsMappedList() {
        when(userUseCase.getAllUsers()).thenReturn(List.of(student));
        when(userMapper.toResponseInfo(student)).thenReturn(userResponseInfo);

        List<UserResponseInfo> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userUseCase).getAllUsers();
    }

    @Test
    void whenGetAllUsersReturnsEmpty_thenResultIsEmpty() {
        when(userUseCase.getAllUsers()).thenReturn(List.of());

        List<UserResponseInfo> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }
}
