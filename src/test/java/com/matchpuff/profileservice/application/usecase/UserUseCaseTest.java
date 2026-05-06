package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.exceptions.InvalidImageInputException;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.application.service.PasswordHashingService;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
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
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private ImageStoragePort imageStoragePort;

    @Mock
    private PasswordHashingService passwordHashingService;

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

    @Test
    void givenValidAdmin_whenCreateAdminUser_thenRepositorySaveIsCalled() {
        Admin admin = new Admin();
        admin.setId("admin-1");
        admin.setName("Root Admin");
        when(userRepository.save(admin)).thenReturn(admin);

        User result = userUseCase.createAdminUser(admin);

        assertEquals("admin-1", result.getId());
        verify(userRepository).save(admin);
    }

    @Test
    void givenValidOrganizer_whenCreateOrganizerUser_thenRepositorySaveIsCalled() {
        Organizer organizer = new Organizer();
        organizer.setId("org-1");
        organizer.setName("Club Organizers");
        when(userRepository.save(organizer)).thenReturn(organizer);

        User result = userUseCase.createOrganizerUser(organizer);

        assertEquals("org-1", result.getId());
        verify(userRepository).save(organizer);
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
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals("Nuevo Nombre", result.getName());
    }

    @Test
    void givenExistingUser_whenUpdateWithCareer_thenCareerIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setCareer(CareerEnum.COMPUTER_SCIENCE);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals(CareerEnum.COMPUTER_SCIENCE, ((StudentProfile) result).getCareer());
    }

    @Test
    void givenExistingUser_whenUpdateWithSemesterZero_thenSemesterIsNotUpdated() {
        StudentProfile request = new StudentProfile();
        request.setSemester(0);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals(4, ((StudentProfile) result).getSemester());
    }

    @Test
    void givenExistingUser_whenUpdateWithSemester_thenSemesterIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setSemester(8);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.updateStudentUser("user-1", request);

        assertEquals(8, ((StudentProfile) result).getSemester());
    }

    @Test
    void givenStudent_whenUpdateUser_thenDelegatesToStudentUpdate() {
        StudentProfile request = new StudentProfile();
        request.setName("Nuevo Nombre");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.updateUser("user-1", request);

        assertEquals("Nuevo Nombre", result.getName());
        verify(userRepository).update("user-1", student);
    }

    @Test
    void givenAdmin_whenUpdateUser_thenDelegatesToAdminUpdate() {
        Admin admin = new Admin();
        admin.setId("admin-1");
        admin.setName("Admin Original");

        Admin request = new Admin();
        request.setName("Admin Nuevo");

        when(userRepository.findById("admin-1")).thenReturn(Optional.of(admin));
        when(userRepository.update("admin-1", admin)).thenReturn(admin);

        User result = userUseCase.updateUser("admin-1", request);

        assertEquals("Admin Nuevo", result.getName());
        verify(userRepository).update("admin-1", admin);
    }

    @Test
    void givenOrganizer_whenUpdateUser_thenDelegatesToOrganizerUpdate() {
        Organizer organizer = new Organizer();
        organizer.setId("org-1");
        organizer.setName("Organizer Original");

        Organizer request = new Organizer();
        request.setName("Organizer Nuevo");

        when(userRepository.findById("org-1")).thenReturn(Optional.of(organizer));
        when(userRepository.update("org-1", organizer)).thenReturn(organizer);

        User result = userUseCase.updateUser("org-1", request);

        assertEquals("Organizer Nuevo", result.getName());
        verify(userRepository).update("org-1", organizer);
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

    @Test
    void givenValidCurrentPassword_whenChangePassword_thenHashesAndUpdatesUser() {
        student.setPasswordHash("CurrentPassword123");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(passwordHashingService.verifyPassword("CurrentPassword123", "CurrentPassword123")).thenReturn(true);
        when(passwordHashingService.hashPassword("NewPassword123")).thenReturn("HashedNewPassword123");
        when(userRepository.update("user-1", student)).thenReturn(student);

        userUseCase.changePassword("user-1", "CurrentPassword123", "NewPassword123");

        assertEquals("HashedNewPassword123", student.getPasswordHash());
        verify(userRepository).update("user-1", student);
    }

    @Test
    void givenWrongCurrentPassword_whenChangePassword_thenThrowsProfileServiceException() {
        student.setPasswordHash("CurrentPassword123");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(passwordHashingService.verifyPassword("BadPassword123", "CurrentPassword123")).thenReturn(false);

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.changePassword("user-1", "BadPassword123", "NewPassword123")
        );

        verify(userRepository, never()).update(anyString(), any());
    }

    // ── addScheduleToStudent ──────────────────────────────────────

    @Test
    void givenExistingStudent_whenAddSchedule_thenScheduleIsAdded() {
        Schedule schedule = new Schedule( DayOfWeekEnum.MONDAY, "Cálculo I", LocalTime.of(8, 0), LocalTime.of(10, 0));


        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent("user-1", schedule);

        assertEquals(1, ((StudentProfile) result).getSchedules().size());
        assertEquals("Cálculo I", ((StudentProfile) result).getSchedules().get(0).getName());
    }

    @Test
    void givenStudentWithNullSchedules_whenAddSchedule_thenListIsInitialized() {
        student.setSchedules(null);
        Schedule schedule = new Schedule( DayOfWeekEnum.TUESDAY, "Física", LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent("user-1", schedule);

        assertNotNull(((StudentProfile) result).getSchedules());
        assertEquals(1, ((StudentProfile) result).getSchedules().size());
    }

    @Test
    void givenNonExistingUser_whenAddSchedule_thenThrowsProfileServiceException() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        Schedule schedule = new Schedule( DayOfWeekEnum.WEDNESDAY, "Química", LocalTime.of(12, 0), LocalTime.of(14, 0));

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
        when(userRepository.update("user-1", student)).thenReturn(student);

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
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.addTagToStudent("user-1", tag);

        assertNotNull(((StudentProfile) result).getTags());
        assertEquals(1, ((StudentProfile) result).getTags().size());
    }

    @Test
    void givenStudentWithImmutableTags_whenAddTag_thenListIsCopiedAndTagIsAdded() {
        Tag existingTag = new Tag();
        existingTag.setName("Java");
        student.setTags(List.of(existingTag));

        Tag newTag = new Tag();
        newTag.setName("Kotlin");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.addTagToStudent("user-1", newTag);

        assertEquals(2, ((StudentProfile) result).getTags().size());
        assertEquals("Kotlin", ((StudentProfile) result).getTags().get(1).getName());
    }

    @Test
    void givenStudentWithImmutableSchedules_whenAddSchedule_thenListIsCopiedAndScheduleIsAdded() {
        Schedule existingSchedule = new Schedule(DayOfWeekEnum.MONDAY, "Base", LocalTime.of(8, 0), LocalTime.of(10, 0));
        student.setSchedules(List.of(existingSchedule));

        Schedule newSchedule = new Schedule(DayOfWeekEnum.TUESDAY, "Nuevo", LocalTime.of(10, 0), LocalTime.of(12, 0));

        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent("user-1", newSchedule);

        assertEquals(2, ((StudentProfile) result).getSchedules().size());
        assertEquals("Nuevo", ((StudentProfile) result).getSchedules().get(1).getName());
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

    // ── deleteUser ───────────────────────────────────────────────

    @Test
    void givenExistingUser_whenDeleteUser_thenDeletesFromRepository() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));

        userUseCase.deleteUser("user-1");

        verify(userRepository).delete("user-1");
    }

    @Test
    void givenMissingUser_whenDeleteUser_thenThrowsAndDoesNotDelete() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ProfileServiceException.class, () -> userUseCase.deleteUser("missing"));
        verify(userRepository, never()).delete(anyString());
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

    @Test
    void whenGetAllStudentProfiles_thenReturnsListFromRepository() {
        when(userRepository.findAllStudents()).thenReturn(List.of(student));

        List<StudentProfile> result = userUseCase.getAllStudentProfiles();

        assertEquals(1, result.size());
        verify(userRepository).findAllStudents();
    }

    // ── updateProfileImage ───────────────────────────────────────

    @Test
    void givenNullFile_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        InvalidImageInputException ex = assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage("user-1", null, "image/png")
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenEmptyFile_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        byte[] file = new byte[0];

        assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage("user-1", file, "image/png")
        );
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenFileLargerThan5Mb_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        byte[] tooLarge = new byte[(5 * 1024 * 1024) + 1];

        assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage("user-1", tooLarge, "image/jpeg")
        );
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenUnsupportedContentType_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        byte[] file = new byte[128];

        assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage("user-1", file, "text/plain")
        );
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenMissingUser_whenUpdateProfileImage_thenThrowsProfileServiceExceptionNotFound() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        ProfileServiceException ex = assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.updateProfileImage("missing", new byte[10], "image/png")
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenNonStudentUser_whenUpdateProfileImage_thenThrowsProfileServiceExceptionBadRequest() {
        Admin admin = new Admin();
        admin.setId("admin-1");
        when(userRepository.findById("admin-1")).thenReturn(Optional.of(admin));

        ProfileServiceException ex = assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.updateProfileImage("admin-1", new byte[100], "image/jpeg")
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenStudentAndValidImage_whenUpdateProfileImage_thenUploadsAndPersistsPhotoUrl() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(student));
        when(imageStoragePort.uploadProfileImage(any(), eq("user-1"))).thenReturn("https://cdn/new-photo.jpg");
        when(userRepository.update("user-1", student)).thenReturn(student);

        User result = userUseCase.updateProfileImage("user-1", new byte[256], "image/jpeg");

        assertEquals("https://cdn/new-photo.jpg", ((StudentProfile) result).getPhotoUrl());
        verify(imageStoragePort).uploadProfileImage(any(), eq("user-1"));
        verify(userRepository).update("user-1", student);
    }
}
