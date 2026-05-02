package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private StudentProfile student;

    @BeforeEach
    void setUp() {
        student = new StudentProfile();
        student.setId("user-1");
        student.setName("Carlos Perez");
        student.setEmail("carlos@escuelaing.edu.co");
        student.setSemester(4);
        student.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        student.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        student.setSchedules(new ArrayList<>());
        student.setTags(new ArrayList<>());
    }

    // ── createStudentUser ─────────────────────────────────────────

    @Test
    void givenValidStudent_whenCreateStudentUser_thenRepositorySaveIsCalled() {
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.createStudentUser(student);

        assertNotNull(result);
        verify(userRepository).save(student);
    }

    @Test
    void givenValidStudent_whenCreateStudentUser_thenReturnsSavedUser() {
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.createStudentUser(student);

        assertEquals("Carlos Perez", result.getName());
    }

    // ── getUser ───────────────────────────────────────────────────

    @Test
    void givenExistingId_whenGetUser_thenReturnsUser() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));

        User result = userUseCase.getUser("user-1");

        assertNotNull(result);
        assertEquals("user-1", result.getId());
    }

    @Test
    void givenNonExistingId_whenGetUser_thenThrowsProfileServiceException() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        ProfileServiceException ex = assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.getUser("unknown")
        );
        assertTrue(ex.getMessage().contains("unknown"));
    }

    // ── updateStudentUser ─────────────────────────────────────────

    @Test
    void givenExistingUser_whenUpdateWithName_thenNameIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setName("Nuevo Nombre");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals("Nuevo Nombre", result.getName());
    }

    @Test
    void givenExistingUser_whenUpdateWithCareer_thenCareerIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setCareer(CareerEnum.COMPUTER_SCIENCE);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals(CareerEnum.COMPUTER_SCIENCE, ((StudentProfile) result).getCareer());
    }

    @Test
    void givenExistingUser_whenUpdateWithSemesterZero_thenSemesterIsNotUpdated() {
        StudentProfile request = new StudentProfile();
        request.setSemester(0);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals(4, ((StudentProfile) result).getSemester());
    }

    @Test
    void givenExistingUser_whenUpdateWithSemester_thenSemesterIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setSemester(8);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals(8, ((StudentProfile) result).getSemester());
    }

    @Test
    void givenNonExistingUser_whenUpdate_thenThrowsProfileServiceException() {
        when(userRepository.findById("ghost")).thenReturn(Optional.empty());

        StudentProfile studentProfile = new StudentProfile();

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.updateStudentUser("ghost", studentProfile)
        );
    }

    // ── addScheduleToStudent ──────────────────────────────────────

    @Test
    void givenExistingStudent_whenAddSchedule_thenScheduleIsAdded() {
        Schedule schedule = new Schedule();
        schedule.setName("Cálculo I");
        schedule.setDayOfWeek(DayOfWeekEnum.MONDAY);
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(10, 0));

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent("user-1", schedule);

        assertEquals(1, ((StudentProfile) result).getSchedules().size());
        assertEquals("Cálculo I", ((StudentProfile) result).getSchedules().get(0).getName());
    }

    @Test
    void givenStudentWithNullSchedules_whenAddSchedule_thenListIsInitialized() {
        student.setSchedules(null);
        Schedule schedule = new Schedule();
        schedule.setName("Física");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent("user-1", schedule);

        assertNotNull(((StudentProfile) result).getSchedules());
        assertEquals(1, ((StudentProfile) result).getSchedules().size());
    }

    @Test
    void givenNonExistingUser_whenAddSchedule_thenThrowsProfileServiceException() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        Schedule schedule = new Schedule();

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.addScheduleToStudent("missing", schedule)
        );
    }

    // ── addTagToStudent ───────────────────────────────────────────

    @Test
    void givenExistingStudent_whenAddTag_thenTagIsAdded() {
        Tag tag = new Tag();
        tag.setName("Machine Learning");
        tag.setCategory("IA");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.addTagToStudent("user-1", tag);

        assertEquals(1, ((StudentProfile) result).getTags().size());
        assertEquals("Machine Learning", ((StudentProfile) result).getTags().get(0).getName());
    }

    @Test
    void givenStudentWithNullTags_whenAddTag_thenListIsInitialized() {
        student.setTags(null);
        Tag tag = new Tag();
        tag.setName("Kotlin");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.save(student)).thenReturn(student);

        User result = userUseCase.addTagToStudent("user-1", tag);

        assertNotNull(((StudentProfile) result).getTags());
        assertEquals(1, ((StudentProfile) result).getTags().size());
    }

    @Test
    void givenNonExistingUser_whenAddTag_thenThrowsProfileServiceException() {
        when(userRepository.findById("nobody")).thenReturn(Optional.empty());

        Tag tag = new Tag();

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.addTagToStudent("nobody", tag)
        );
    }

    // ── getAllUsers ────────────────────────────────────────────────

    @Test
    void whenGetAllUsers_thenReturnsListFromRepository() {
        when(userRepository.findAll()).thenReturn(List.of(student));

        List<User> result = userUseCase.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void whenRepositoryReturnsEmptyList_thenGetAllUsersReturnsEmpty() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = userUseCase.getAllUsers();

        assertTrue(result.isEmpty());
    }
}
