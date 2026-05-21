package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.exceptions.InvalidImageInputException;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.application.service.PasswordHashingService;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
import com.matchpuff.profileservice.domain.ports.out.TagCatalogPort;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Mock
    private TagCatalogPort tagCatalogPort;

    @InjectMocks
    private UserUseCase userUseCase;

    private StudentProfile student;

    @BeforeEach
    void setUp() {
        student = new StudentProfile();
        student.setId(UUID.randomUUID());
        student.setName("Carlos Perez");
        student.setEmail("carlos@escuelaing.edu.co");
        student.setSemester(4);
        student.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        student.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        student.setSchedulesAvailability(new ArrayList<>());
        student.setTagsId(new ArrayList<>());

        lenient().when(tagCatalogPort.tagExists(any(UUID.class))).thenReturn(true);
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
        admin.setId(UUID.randomUUID());
        admin.setName("Root Admin");
        when(userRepository.save(admin)).thenReturn(admin);

        User result = userUseCase.createAdminUser(admin);

        assertEquals(admin.getId(), result.getId());
        verify(userRepository).save(admin);
    }

    @Test
    void givenValidOrganizer_whenCreateOrganizerUser_thenRepositorySaveIsCalled() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Club Organizers");
        when(userRepository.save(organizer)).thenReturn(organizer);

        User result = userUseCase.createOrganizerUser(organizer);

        assertEquals(organizer.getId(), result.getId());
        verify(userRepository).save(organizer);
    }

    // ── getUser ───────────────────────────────────────────────────

    @Test
    void givenExistingId_whenGetUser_thenReturnsUser() {
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        User result = userUseCase.getUser(student.getId());

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
    }

    @Test
    void givenNonExistingId_whenGetUser_thenThrowsProfileServiceException() {
        UUID unknownId = UUID.randomUUID();
        when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

        ProfileServiceException ex = assertThrows(
            ProfileServiceException.class,
            () -> userUseCase.getUser(unknownId)
        );
        assertTrue(ex.getMessage().contains("not found"));
    }

    // ── updateStudentUser ─────────────────────────────────────────

    @Test
    void givenExistingUser_whenUpdateWithName_thenNameIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setName("Nuevo Nombre");

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateStudentUser(student.getId(), request);

        assertEquals("Nuevo Nombre", result.getName());
    }

    @Test
    void givenExistingUser_whenUpdateWithCareer_thenCareerIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setCareer(CareerEnum.COMPUTER_SCIENCE);

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateStudentUser(student.getId(), request);

        assertEquals(CareerEnum.COMPUTER_SCIENCE, ((StudentProfile) result).getCareer());
    }

    @Test
    void givenExistingUser_whenUpdateWithNullSemester_thenSemesterIsNotUpdated() {
        StudentProfile request = new StudentProfile();
        // semester null by default — should not overwrite existing value

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateStudentUser(student.getId(), request);

        assertEquals(4, ((StudentProfile) result).getSemester());
    }

    @Test
    void givenExistingUser_whenUpdateWithSemester_thenSemesterIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setSemester(8);

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateStudentUser(student.getId(), request);

        assertEquals(8, ((StudentProfile) result).getSemester());
    }

    @Test
    void givenStudent_whenUpdateUser_thenDelegatesToStudentUpdate() {
        StudentProfile request = new StudentProfile();
        request.setName("Nuevo Nombre");

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateUser(student.getId(), request);

        assertEquals("Nuevo Nombre", result.getName());
        verify(userRepository).update(student.getId(), student);
    }

    @Test
    void givenAdmin_whenUpdateUser_thenDelegatesToAdminUpdate() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setName("Admin Original");

        Admin request = new Admin();
        request.setName("Admin Nuevo");

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(userRepository.update(admin.getId(), admin)).thenReturn(admin);

        User result = userUseCase.updateUser(admin.getId(), request);

        assertEquals("Admin Nuevo", result.getName());
        verify(userRepository).update(admin.getId(), admin);
    }

    @Test
    void givenOrganizer_whenUpdateUser_thenDelegatesToOrganizerUpdate() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Organizer Original");

        Organizer request = new Organizer();
        request.setName("Organizer Nuevo");

        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));
        when(userRepository.update(organizer.getId(), organizer)).thenReturn(organizer);

        User result = userUseCase.updateUser(organizer.getId(), request);

        assertEquals("Organizer Nuevo", result.getName());
        verify(userRepository).update(organizer.getId(), organizer);
    }

    @Test
    void givenNonExistingUser_whenUpdate_thenThrowsProfileServiceException() {
        UUID ghostId = UUID.randomUUID();
        when(userRepository.findById(ghostId)).thenReturn(Optional.empty());

        StudentProfile studentProfile = new StudentProfile();

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.updateStudentUser(ghostId, studentProfile)
        );
    }

    @Test
    void givenValidCurrentPassword_whenChangePassword_thenHashesAndUpdatesUser() {
        student.setPasswordHash("Current.Password123");

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(passwordHashingService.verifyPassword("Current.Password123", "Current.Password123")).thenReturn(true);
        when(passwordHashingService.hashPassword("New.Password123")).thenReturn("HashedNew.Password123");
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        userUseCase.changePassword(student.getId(), "Current.Password123", "New.Password123");

        assertEquals("HashedNew.Password123", student.getPasswordHash());
        verify(userRepository).update(student.getId(), student);
    }

    @Test
    void givenWrongCurrentPassword_whenChangePassword_thenThrowsProfileServiceException() {
        student.setPasswordHash("Current@Password123");

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(passwordHashingService.verifyPassword("BadPassword123", "Current@Password123")).thenReturn(false);

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.changePassword(student.getId(), "BadPassword123", "NewPassword123")
        );

        verify(userRepository, never()).update(any(UUID.class), any());
    }

    // ── addScheduleToStudent ──────────────────────────────────────

    @Test
    void givenExistingStudent_whenAddSchedule_thenScheduleIsAdded() {
        Schedule schedule = new Schedule( DayOfWeekEnum.MONDAY, "Cálculo I", LocalTime.of(8, 0), LocalTime.of(9, 30));


        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent(student.getId(), schedule);

        assertEquals(1, ((StudentProfile) result).getSchedulesAvailability().size());
        assertEquals("Cálculo I", ((StudentProfile) result).getSchedulesAvailability().get(0).getName());
    }

    @Test
    void givenStudentWithNullSchedules_whenAddSchedule_thenListIsInitialized() {
        student.setSchedulesAvailability(null);
        Schedule schedule = new Schedule( DayOfWeekEnum.TUESDAY, "Física", LocalTime.of(10, 0), LocalTime.of(11, 30));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent(student.getId(), schedule);

        assertNotNull(((StudentProfile) result).getSchedulesAvailability());
        assertEquals(1, ((StudentProfile) result).getSchedulesAvailability().size());
    }

    @Test
    void givenNonExistingUser_whenAddSchedule_thenThrowsProfileServiceException() {
        UUID missingId = UUID.randomUUID();
        when(userRepository.findById(missingId)).thenReturn(Optional.empty());

        Schedule schedule = new Schedule( DayOfWeekEnum.WEDNESDAY, "Química", LocalTime.of(12, 0), LocalTime.of(13, 30));

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.addScheduleToStudent(missingId, schedule)
        );
    }

    // ── addTagToStudent ───────────────────────────────────────────

    @Test
    void givenExistingStudent_whenAddTag_thenTagIsAdded() {
        java.util.UUID tagId = UUID.randomUUID();

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.addTagToStudent(student.getId(), tagId);

        assertEquals(1, ((StudentProfile) result).getTagsId().size());
        assertEquals(tagId, ((StudentProfile) result).getTagsId().get(0));
    }

    @Test
    void givenStudentWithNullTags_whenAddTag_thenListIsInitialized() {
        student.setTagsId(null);
        java.util.UUID tagId = UUID.randomUUID();

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.addTagToStudent(student.getId(), tagId);

        assertNotNull(((StudentProfile) result).getTagsId());
        assertEquals(1, ((StudentProfile) result).getTagsId().size());
    }

    @Test
    void givenStudentWithImmutableTags_whenAddTag_thenListIsCopiedAndTagIsAdded() {
        java.util.UUID existing = UUID.randomUUID();
        student.setTagsId(List.of(existing));

        java.util.UUID newTag = UUID.randomUUID();

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.addTagToStudent(student.getId(), newTag);

        assertEquals(2, ((StudentProfile) result).getTagsId().size());
        assertEquals(newTag, ((StudentProfile) result).getTagsId().get(1));
    }

    @Test
    void givenStudentWithImmutableSchedules_whenAddSchedule_thenListIsCopiedAndScheduleIsAdded() {
        Schedule existingSchedule = new Schedule(DayOfWeekEnum.MONDAY, "Base", LocalTime.of(8, 0), LocalTime.of(9, 30));
        student.setSchedulesAvailability(List.of(existingSchedule));

        Schedule newSchedule = new Schedule(DayOfWeekEnum.TUESDAY, "Nuevo", LocalTime.of(10, 0), LocalTime.of(11, 30));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.addScheduleToStudent(student.getId(), newSchedule);

        assertEquals(2, ((StudentProfile) result).getSchedulesAvailability().size());
        assertEquals("Nuevo", ((StudentProfile) result).getSchedulesAvailability().get(1).getName());
    }

    @Test
    void givenStudentWithExistingSchedule_whenAddOverlappingSchedule_thenThrowsInvalidInputException() {
        Schedule existing = new Schedule(DayOfWeekEnum.MONDAY, "Calculo", LocalTime.of(8, 0), LocalTime.of(9, 30));
        student.setSchedulesAvailability(new ArrayList<>(List.of(existing)));

        Schedule overlapping = new Schedule(DayOfWeekEnum.MONDAY, "Fisica", LocalTime.of(9, 0), LocalTime.of(10, 30));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        assertThrows(InvalidInputException.class,
                () -> userUseCase.addScheduleToStudent(student.getId(), overlapping));
    }

    @Test
    void givenNonExistingUser_whenAddTag_thenThrowsProfileServiceException() {
        UUID nobody = UUID.randomUUID();
        when(userRepository.findById(nobody)).thenReturn(Optional.empty());

        java.util.UUID tagId = UUID.randomUUID();

        assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.addTagToStudent(nobody, tagId)
        );
    }

    @Test
    void givenStudentWithExistingTag_whenAddSameTag_thenThrowsConflict() {
        java.util.UUID tagId = UUID.randomUUID();
        student.setTagsId(new ArrayList<>(List.of(tagId)));

        UUID studentId = student.getId();
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));

        ProfileServiceException ex = assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.addTagToStudent(studentId, tagId)
        );
        assertEquals(org.springframework.http.HttpStatus.CONFLICT, ex.getStatus());
    }

    // ── removeScheduleFromStudent / removeTagFromStudent ─────────

    @Test
    void givenExistingSchedule_whenRemoveSchedule_thenScheduleIsRemoved() {
        Schedule schedule = new Schedule(DayOfWeekEnum.MONDAY, "Calculo", LocalTime.of(8, 0), LocalTime.of(9, 30));
        student.setSchedulesAvailability(new ArrayList<>(List.of(schedule)));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.removeScheduleFromStudent(student.getId(), schedule);

        assertTrue(((StudentProfile) result).getSchedulesAvailability().isEmpty());
        verify(userRepository).update(student.getId(), student);
    }

    @Test
    void givenMissingSchedule_whenRemoveSchedule_thenThrowsProfileServiceException() {
        Schedule existing = new Schedule(DayOfWeekEnum.MONDAY, "Calculo", LocalTime.of(8, 0), LocalTime.of(9, 30));
        Schedule toRemove = new Schedule(DayOfWeekEnum.TUESDAY, "Fisica", LocalTime.of(10, 0), LocalTime.of(11, 30));
        student.setSchedulesAvailability(new ArrayList<>(List.of(existing)));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        assertThrows(ProfileServiceException.class, () -> userUseCase.removeScheduleFromStudent(student.getId(), toRemove));
        verify(userRepository, never()).update(any(UUID.class), any());
    }

    @Test
    void givenExistingTagInImmutableList_whenRemoveTag_thenTagIsRemoved() {
        UUID tagId = UUID.randomUUID();
        student.setTagsId(List.of(tagId));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.removeTagFromStudent(student.getId(), tagId);

        assertTrue(((StudentProfile) result).getTagsId().isEmpty());
        verify(userRepository).update(student.getId(), student);
    }

    @Test
    void givenMissingTag_whenRemoveTag_thenThrowsProfileServiceException() {
        UUID existingTagId = UUID.randomUUID();
        UUID toRemoveTagId = UUID.randomUUID();
        student.setTagsId(new ArrayList<>(List.of(existingTagId)));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        assertThrows(ProfileServiceException.class, () -> userUseCase.removeTagFromStudent(student.getId(), toRemoveTagId));
        verify(userRepository, never()).update(any(UUID.class), any());
    }

    // ── deleteUser ───────────────────────────────────────────────

    @Test
    void givenExistingUser_whenDeleteUser_thenDeletesFromRepository() {
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        userUseCase.deleteUser(student.getId());

        verify(userRepository).delete(student.getId());
    }

    @Test
    void givenMissingUser_whenDeleteUser_thenThrowsAndDoesNotDelete() {
        UUID missingId = UUID.randomUUID();
        when(userRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(ProfileServiceException.class, () -> userUseCase.deleteUser(missingId));
        verify(userRepository, never()).delete(any(UUID.class));
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
                () -> userUseCase.updateProfileImage(student.getId(), null, "image/png")
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenEmptyFile_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        byte[] file = new byte[0];

        assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage(student.getId(), file, "image/png")
        );
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenFileLargerThan5Mb_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        byte[] tooLarge = new byte[(5 * 1024 * 1024) + 1];

        assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage(student.getId(), tooLarge, "image/jpeg")
        );
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenUnsupportedContentType_whenUpdateProfileImage_thenThrowsInvalidImageInputException() {
        byte[] file = new byte[128];

        assertThrows(
                InvalidImageInputException.class,
                () -> userUseCase.updateProfileImage(student.getId(), file, "text/plain")
        );
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenMissingUser_whenUpdateProfileImage_thenThrowsProfileServiceExceptionNotFound() {
        when(userRepository.findById(student.getId())).thenReturn(Optional.empty());

        ProfileServiceException ex = assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.updateProfileImage(student.getId(), new byte[10], "image/png")
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenNonStudentUser_whenUpdateProfileImage_thenThrowsProfileServiceExceptionBadRequest() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        ProfileServiceException ex = assertThrows(
                ProfileServiceException.class,
                () -> userUseCase.updateProfileImage(admin.getId(), new byte[100], "image/jpeg")
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verifyNoInteractions(imageStoragePort);
    }

    @Test
    void givenStudentAndValidImage_whenUpdateProfileImage_thenUploadsAndPersistsPhotoUrl() {
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(imageStoragePort.uploadProfileImage(any(), eq(student.getId()))).thenReturn("https://cdn/new-photo.jpg");
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateProfileImage(student.getId(), new byte[256], "image/jpeg");

        assertEquals("https://cdn/new-photo.jpg", ((StudentProfile) result).getPhotoUrl());
        verify(imageStoragePort).uploadProfileImage(any(), eq(student.getId()));
        verify(userRepository).update(student.getId(), student);
    }

    // ── getUserByEmail ────────────────────────────────────────────

    @Test
    void givenExistingEmail_whenGetUserByEmail_thenReturnsUser() {
        when(userRepository.findByEmail("carlos@escuelaing.edu.co")).thenReturn(Optional.of(student));

        User result = userUseCase.getUserByEmail("carlos@escuelaing.edu.co");

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
    }

    @Test
    void givenNonExistingEmail_whenGetUserByEmail_thenThrowsProfileServiceException() {
        when(userRepository.findByEmail("nobody@escuelaing.edu.co")).thenReturn(Optional.empty());

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.getUserByEmail("nobody@escuelaing.edu.co"));
    }

    // ── verifyUser ────────────────────────────────────────────────

    @Test
    void givenUnverifiedUser_whenVerifyUser_thenSetsVerifiedTrueAndUpdates() {
        student.setVerified(false);
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        userUseCase.verifyUser(student.getId());

        assertTrue(student.isVerified());
        verify(userRepository).update(student.getId(), student);
    }

    @Test
    void givenAlreadyVerifiedUser_whenVerifyUser_thenThrowsProfileServiceException() {
        student.setVerified(true);
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        assertThrows(ProfileServiceException.class, () -> userUseCase.verifyUser(student.getId()));
        verify(userRepository, never()).update(any(UUID.class), any());
    }

    @Test
    void givenNonExistingUser_whenVerifyUser_thenThrowsProfileServiceException() {
        UUID ghostId = UUID.randomUUID();
        when(userRepository.findById(ghostId)).thenReturn(Optional.empty());

        assertThrows(ProfileServiceException.class, () -> userUseCase.verifyUser(ghostId));
    }

    // ── updateGeolocation ─────────────────────────────────────────

    @Test
    void givenStudent_whenUpdateGeolocationEnabled_thenUpdatesFlagToTrue() {
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateGeolocation(student.getId(), true);

        assertTrue(((StudentProfile) result).isGeolocationEnabled());
        verify(userRepository).update(student.getId(), student);
    }

    @Test
    void givenStudent_whenUpdateGeolocationDisabled_thenUpdatesFlagToFalse() {
        student.setGeolocationEnabled(true);
        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateGeolocation(student.getId(), false);

        assertFalse(((StudentProfile) result).isGeolocationEnabled());
    }

    @Test
    void givenAdmin_whenUpdateGeolocation_thenThrowsProfileServiceException() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.updateGeolocation(admin.getId(), true));
        verify(userRepository, never()).update(any(UUID.class), any());
    }

    @Test
    void givenNonExistingUser_whenUpdateGeolocation_thenThrowsProfileServiceException() {
        UUID missingId = UUID.randomUUID();
        when(userRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.updateGeolocation(missingId, true));
    }

    // ── updateStudentUser additional fields ───────────────────────

    @Test
    void givenExistingStudent_whenUpdateWithPassword_thenPasswordIsHashed() {
        StudentProfile request = new StudentProfile();
        request.setPasswordHash("NewP@ssword1");

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(passwordHashingService.hashPassword("NewP@ssword1")).thenReturn("HashedNewP@ssword1");
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        userUseCase.updateStudentUser(student.getId(), request);

        assertEquals("HashedNewP@ssword1", student.getPasswordHash());
        verify(passwordHashingService).hashPassword("NewP@ssword1");
    }

    @Test
    void givenExistingStudent_whenUpdateWithSchedulesAndTags_thenBothAreUpdated() {
        Schedule schedule = new Schedule(DayOfWeekEnum.MONDAY, "Calculo", LocalTime.of(8, 0), LocalTime.of(9, 30));
        UUID tagId = UUID.randomUUID();

        StudentProfile request = new StudentProfile();
        request.setSchedulesAvailability(List.of(schedule));
        request.setTagsId(List.of(tagId));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateStudentUser(student.getId(), request);

        assertEquals(1, ((StudentProfile) result).getSchedulesAvailability().size());
        assertEquals(1, ((StudentProfile) result).getTagsId().size());
    }

    @Test
    void givenExistingStudent_whenUpdateWithDateOfBirth_thenDateOfBirthIsUpdated() {
        StudentProfile request = new StudentProfile();
        request.setDateOfBirth(LocalDate.of(1998, 5, 20));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(userRepository.update(student.getId(), student)).thenReturn(student);

        User result = userUseCase.updateStudentUser(student.getId(), request);

        assertEquals(LocalDate.of(1998, 5, 20), result.getDateOfBirth());
    }

    // ── updateAdminUser ───────────────────────────────────────────

    @Test
    void givenExistingAdmin_whenUpdateAdminWithGender_thenGenderIsUpdated() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setName("Old Admin");

        Admin request = new Admin();
        request.setGender(GenderEnum.FEMALE);

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(userRepository.update(admin.getId(), admin)).thenReturn(admin);

        User result = userUseCase.updateAdminUser(admin.getId(), request);

        assertEquals(GenderEnum.FEMALE, result.getGender());
    }

    @Test
    void givenExistingAdmin_whenUpdateAdminWithPassword_thenPasswordIsHashed() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());

        Admin request = new Admin();
        request.setPasswordHash("New@minPass1");

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(passwordHashingService.hashPassword("New@minPass1")).thenReturn("New@minPass1");
        when(userRepository.update(admin.getId(), admin)).thenReturn(admin);

        userUseCase.updateAdminUser(admin.getId(), request);

        assertEquals("New@minPass1", admin.getPasswordHash());
    }

    @Test
    void givenStudentReturnedForAdminId_whenUpdateAdmin_thenThrowsClassCastException() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(student));

        assertThrows(ClassCastException.class,
                () -> userUseCase.updateAdminUser(admin.getId(), new Admin()));
    }

    // ── updateOrganizerUser ───────────────────────────────────────

    @Test
    void givenExistingOrganizer_whenUpdateOrganizerWithContact_thenContactIsUpdated() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Org");

        Organizer request = new Organizer();
        request.setContactInfo("new@contact.co");

        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));
        when(userRepository.update(organizer.getId(), organizer)).thenReturn(organizer);

        User result = userUseCase.updateOrganizerUser(organizer.getId(), request);

        assertEquals("new@contact.co", ((Organizer) result).getContactInfo());
    }

    @Test
    void givenExistingOrganizer_whenUpdateOrganizerWithPassword_thenPasswordIsHashed() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());

        Organizer request = new Organizer();
        request.setPasswordHash("New@rgPass1");

        when(userRepository.findById(organizer.getId())).thenReturn(Optional.of(organizer));
        when(passwordHashingService.hashPassword("New@rgPass1")).thenReturn("New@rgPass1");
        when(userRepository.update(organizer.getId(), organizer)).thenReturn(organizer);

        userUseCase.updateOrganizerUser(organizer.getId(), request);

        assertEquals("New@rgPass1", organizer.getPasswordHash());
    }

    // ── updateUser with unsupported type ─────────────────────────

    @Test
    void givenUnsupportedUserType_whenUpdateUser_thenThrowsProfileServiceException() {
        User generic = new User() {};
        UUID userId = UUID.randomUUID();

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.updateUser(userId, generic));
    }

    // ── addScheduleToStudent non-student ─────────────────────────

    @Test
    void givenAdmin_whenAddSchedule_thenThrowsProfileServiceException() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        Schedule schedule = new Schedule(DayOfWeekEnum.MONDAY, "Clase", LocalTime.of(8, 0), LocalTime.of(9, 30));

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.addScheduleToStudent(admin.getId(), schedule));
    }

    // ── removeScheduleFromStudent non-student / null list ────────

    @Test
    void givenAdmin_whenRemoveSchedule_thenThrowsProfileServiceException() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        Schedule schedule = new Schedule(DayOfWeekEnum.MONDAY, "Clase", LocalTime.of(8, 0), LocalTime.of(9, 30));

        when(userRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.removeScheduleFromStudent(admin.getId(), schedule));
    }

    @Test
    void givenStudentWithNullScheduleList_whenRemoveSchedule_thenThrowsProfileServiceException() {
        student.setSchedulesAvailability(null);
        Schedule schedule = new Schedule(DayOfWeekEnum.MONDAY, "Clase", LocalTime.of(8, 0), LocalTime.of(9, 30));

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.removeScheduleFromStudent(student.getId(), schedule));
    }

    // ── addTagToStudent non-student ───────────────────────────────

    @Test
    void givenAdmin_whenAddTag_thenThrowsProfileServiceException() {
        Admin admin = new Admin();
        UUID adminId = UUID.randomUUID();
        admin.setId(adminId);
        UUID tagId = UUID.randomUUID();

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.addTagToStudent(adminId, tagId));
    }

    // ── removeTagFromStudent non-student / null list ──────────────

    @Test
    void givenAdmin_whenRemoveTag_thenThrowsProfileServiceException() {
        Admin admin = new Admin();
        UUID adminId = UUID.randomUUID();
        admin.setId(adminId);
        UUID tagId = UUID.randomUUID();

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.removeTagFromStudent(adminId, tagId));
    }

    @Test
    void givenStudentWithNullTagList_whenRemoveTag_thenThrowsProfileServiceException() {
        student.setTagsId(null);
        UUID tagId = UUID.randomUUID();

        when(userRepository.findById(student.getId())).thenReturn(Optional.of(student));

        assertThrows(ProfileServiceException.class,
                () -> userUseCase.removeTagFromStudent(student.getId(), tagId));
    }

    // ── hashPasswordIfPresent (via createStudentUser) ─────────────

    @Test
    void givenStudentWithPassword_whenCreateStudentUser_thenPasswordIsHashed() {
        student.setPasswordHash("Raw.Password1");
        when(passwordHashingService.hashPassword("Raw.Password1")).thenReturn("Raw.Password1");
        when(userRepository.save(student)).thenReturn(student);

        userUseCase.createStudentUser(student);

        assertEquals("Raw.Password1", student.getPasswordHash());
        verify(passwordHashingService).hashPassword("Raw.Password1");
    }

    @Test
    void givenStudentWithNullPassword_whenCreateStudentUser_thenNoHashingOccurs() {
        student.setPasswordHash(null);
        when(userRepository.save(student)).thenReturn(student);

        userUseCase.createStudentUser(student);

        verify(passwordHashingService, never()).hashPassword(any());
    }

    @Test
    void givenAdminWithPassword_whenCreateAdminUser_thenPasswordIsHashed() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setPasswordHash("Admin.Raw1");
        when(passwordHashingService.hashPassword("Admin.Raw1")).thenReturn("Admin.Raw1");
        when(userRepository.save(admin)).thenReturn(admin);

        userUseCase.createAdminUser(admin);

        assertEquals("Admin.Raw1", admin.getPasswordHash());
    }
}
