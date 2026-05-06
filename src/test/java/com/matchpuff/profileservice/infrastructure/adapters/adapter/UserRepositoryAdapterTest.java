package com.matchpuff.profileservice.infrastructure.adapters.adapter;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    private static final String VALID_EMAIL = "test@escuelaing.edu.co";

    private StudentProfile buildStudent() {
        StudentProfile s = new StudentProfile();
        s.setId("u-1");
        s.setName("Test User");
        s.setEmail(VALID_EMAIL);
        s.setGender(GenderEnum.MALE);
        s.setDateOfBirth(LocalDate.of(2000, 1, 1));
        s.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        s.setSemester(3);
        s.setStudentCarnet(Long.valueOf(20211000));
        s.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        return s;
    }

    private StudentProfileDocument buildStudentDoc() {
        StudentProfileDocument doc = new StudentProfileDocument();
        doc.setId("u-1");
        doc.setName("Test User");
        doc.setEmail(VALID_EMAIL);
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.MALE);
        doc.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        doc.setSemester(3);
        doc.setStudentCarnet(Long.valueOf(20211000));
        doc.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        return doc;
    }

    // ── save ──────────────────────────────────────────────────────

    @Test
    void givenNewEmail_whenSave_thenReturnsStudentProfile() {
        StudentProfile student = buildStudent();
        StudentProfileDocument savedDoc = buildStudentDoc();

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.save(student);

        assertNotNull(result);
        assertInstanceOf(StudentProfile.class, result);
        verify(userRepository).save(any());
    }

    @Test
    void givenExistingEmail_whenSave_thenThrowsInvalidInputException() {
        StudentProfile student = buildStudent();
        StudentProfileDocument existingDoc = buildStudentDoc();

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(existingDoc));

        assertThrows(InvalidInputException.class, () -> adapter.save(student));
        verify(userRepository, never()).save(any());
    }

    // ── findById ──────────────────────────────────────────────────

    @Test
    void givenExistingStudentId_whenFindById_thenReturnsStudentProfile() {
        StudentProfileDocument doc = buildStudentDoc();

        when(userRepository.findById("u-1")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findById("u-1");

        assertTrue(result.isPresent());
        assertInstanceOf(StudentProfile.class, result.get());
        assertEquals(VALID_EMAIL, result.get().getEmail());
    }

    @Test
    void givenExistingAdminId_whenFindById_thenReturnsAdmin() {
        AdminProfileDocument doc = new AdminProfileDocument();
        doc.setId("a-1");
        doc.setName("Admin");
        doc.setEmail(VALID_EMAIL);
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.MALE);

        when(userRepository.findById("a-1")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findById("a-1");

        assertTrue(result.isPresent());
        assertInstanceOf(Admin.class, result.get());
    }

    @Test
    void givenExistingOrganizerId_whenFindById_thenReturnsOrganizer() {
        OrganizerProfileDocument doc = new OrganizerProfileDocument();
        doc.setId("o-1");
        doc.setName("Organizer");
        doc.setEmail(VALID_EMAIL);
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.FEMALE);
        doc.setContact("contacto@evento.co");

        when(userRepository.findById("o-1")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findById("o-1");

        assertTrue(result.isPresent());
        assertInstanceOf(Organizer.class, result.get());
        assertEquals("contacto@evento.co", ((Organizer) result.get()).getContactInfo());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnsEmptyOptional() {
        when(userRepository.findById("ghost")).thenReturn(Optional.empty());

        Optional<User> result = adapter.findById("ghost");

        assertFalse(result.isPresent());
    }

    // ── findByEmail ───────────────────────────────────────────────

    @Test
    void givenExistingEmail_whenFindByEmail_thenReturnsUser() {
        StudentProfileDocument doc = buildStudentDoc();

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findByEmail(VALID_EMAIL);

        assertTrue(result.isPresent());
    }

    @Test
    void givenNonExistingEmail_whenFindByEmail_thenReturnsEmpty() {
        when(userRepository.findByEmail("notfound@escuelaing.edu.co")).thenReturn(Optional.empty());

        Optional<User> result = adapter.findByEmail("notfound@escuelaing.edu.co");

        assertFalse(result.isPresent());
    }

    // ── update ────────────────────────────────────────────────────

    @Test
    void givenStudentProfile_whenUpdate_thenSetsIdAndReturnsUser() {
        StudentProfile student = buildStudent();
        StudentProfileDocument currentDoc = buildStudentDoc();
        StudentProfileDocument savedDoc = buildStudentDoc();

        when(userRepository.findById("u-1")).thenReturn(Optional.of(currentDoc));
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update("u-1", student);

        assertNotNull(result);
        assertEquals("u-1", result.getId());
        verify(userRepository).save(any());
    }

    // ── findAll ───────────────────────────────────────────────────

    @Test
    void whenFindAll_thenReturnsMappedList() {
        StudentProfileDocument doc = buildStudentDoc();

        when(userRepository.findAll()).thenReturn(List.of(doc));

        List<User> result = adapter.findAll();

        assertEquals(1, result.size());
        assertInstanceOf(StudentProfile.class, result.get(0));
    }

    @Test
    void whenFindAllReturnsEmpty_thenReturnsEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = adapter.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void givenMixedDocumentTypes_whenFindAll_thenMapsAllCorrectly() {
        StudentProfileDocument studentDoc = buildStudentDoc();

        AdminProfileDocument adminDoc = new AdminProfileDocument();
        adminDoc.setId("a-1");
        adminDoc.setName("Admin");
        adminDoc.setEmail(VALID_EMAIL);

        OrganizerProfileDocument orgDoc = new OrganizerProfileDocument();
        orgDoc.setId("o-1");
        orgDoc.setName("Org");
        orgDoc.setEmail(VALID_EMAIL);

        when(userRepository.findAll()).thenReturn(List.of(studentDoc, adminDoc, orgDoc));

        List<User> result = adapter.findAll();

        assertEquals(3, result.size());
    }

    // ── save Admin ────────────────────────────────────────────────

    @Test
    void givenNewEmail_whenSaveAdmin_thenReturnsAdmin() {
        Admin admin = new Admin();
        admin.setId("a-1");
        admin.setName("Root Admin");
        admin.setEmail(VALID_EMAIL);
        admin.setGender(GenderEnum.MALE);

        AdminProfileDocument savedDoc = new AdminProfileDocument();
        savedDoc.setId("a-1");
        savedDoc.setName("Root Admin");
        savedDoc.setEmail(VALID_EMAIL);
        savedDoc.setGender(GenderEnum.MALE);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.save(admin);

        assertNotNull(result);
        assertInstanceOf(Admin.class, result);
        verify(userRepository).save(any());
    }

    @Test
    void givenExistingEmail_whenSaveAdmin_thenThrowsInvalidInputException() {
        Admin admin = new Admin();
        admin.setEmail(VALID_EMAIL);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(buildStudentDoc()));

        assertThrows(InvalidInputException.class, () -> adapter.save(admin));
        verify(userRepository, never()).save(any());
    }

    // ── save Organizer ────────────────────────────────────────────

    @Test
    void givenNewEmail_whenSaveOrganizer_thenReturnsOrganizer() {
        Organizer organizer = new Organizer();
        organizer.setId("o-1");
        organizer.setName("Club Org");
        organizer.setEmail(VALID_EMAIL);
        organizer.setGender(GenderEnum.FEMALE);
        organizer.setContactInfo("club@evento.co");

        OrganizerProfileDocument savedDoc = new OrganizerProfileDocument();
        savedDoc.setId("o-1");
        savedDoc.setName("Club Org");
        savedDoc.setEmail(VALID_EMAIL);
        savedDoc.setContact("club@evento.co");

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.save(organizer);

        assertNotNull(result);
        assertInstanceOf(Organizer.class, result);
        verify(userRepository).save(any());
    }

    @Test
    void givenExistingEmail_whenSaveOrganizer_thenThrowsInvalidInputException() {
        Organizer organizer = new Organizer();
        organizer.setEmail(VALID_EMAIL);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(buildStudentDoc()));

        assertThrows(InvalidInputException.class, () -> adapter.save(organizer));
        verify(userRepository, never()).save(any());
    }

    // ── delete ────────────────────────────────────────────────────

    @Test
    void givenUserId_whenDelete_thenCallsDeleteById() {
        adapter.delete("u-1");

        verify(userRepository).deleteById("u-1");
    }

    // ── update Admin ──────────────────────────────────────────────

    @Test
    void givenAdminProfile_whenUpdate_thenReturnsUpdatedAdmin() {
        Admin admin = new Admin();
        admin.setId("a-1");
        admin.setName("Updated Admin");
        admin.setEmail(VALID_EMAIL);
        admin.setGender(GenderEnum.MALE);

        AdminProfileDocument currentDoc = new AdminProfileDocument();
        currentDoc.setId("a-1");
        currentDoc.setName("Old Admin");
        currentDoc.setEmail(VALID_EMAIL);
        currentDoc.setGender(GenderEnum.MALE);

        AdminProfileDocument savedDoc = new AdminProfileDocument();
        savedDoc.setId("a-1");
        savedDoc.setName("Updated Admin");
        savedDoc.setEmail(VALID_EMAIL);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(currentDoc));
        when(userRepository.findById("a-1")).thenReturn(Optional.of(currentDoc));
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update("a-1", admin);

        assertNotNull(result);
        assertInstanceOf(Admin.class, result);
        verify(userRepository).save(any());
    }

    // ── update Organizer ──────────────────────────────────────────

    @Test
    void givenOrganizerProfile_whenUpdate_thenReturnsUpdatedOrganizer() {
        Organizer organizer = new Organizer();
        organizer.setId("o-1");
        organizer.setName("Updated Org");
        organizer.setEmail(VALID_EMAIL);
        organizer.setGender(GenderEnum.FEMALE);
        organizer.setContactInfo("new@evento.co");

        OrganizerProfileDocument currentDoc = new OrganizerProfileDocument();
        currentDoc.setId("o-1");
        currentDoc.setName("Old Org");
        currentDoc.setEmail(VALID_EMAIL);
        currentDoc.setContact("old@evento.co");

        OrganizerProfileDocument savedDoc = new OrganizerProfileDocument();
        savedDoc.setId("o-1");
        savedDoc.setName("Updated Org");
        savedDoc.setEmail(VALID_EMAIL);
        savedDoc.setContact("new@evento.co");

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(currentDoc));
        when(userRepository.findById("o-1")).thenReturn(Optional.of(currentDoc));
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update("o-1", organizer);

        assertNotNull(result);
        assertInstanceOf(Organizer.class, result);
        verify(userRepository).save(any());
    }

    // ── update email conflict ─────────────────────────────────────

    @Test
    void givenEmailBelongsToAnotherUser_whenUpdate_thenThrowsInvalidInputException() {
        StudentProfile student = buildStudent();
        student.setEmail("other@escuelaing.edu.co");

        StudentProfileDocument conflictDoc = buildStudentDoc();
        conflictDoc.setId("other-user");
        conflictDoc.setEmail("other@escuelaing.edu.co");

        when(userRepository.findByEmail("other@escuelaing.edu.co")).thenReturn(Optional.of(conflictDoc));

        assertThrows(InvalidInputException.class, () -> adapter.update("u-1", student));
    }

    // ── update type mismatch ──────────────────────────────────────

    @Test
    void givenStudentDocumentButAdminRequest_whenUpdate_thenThrowsInvalidInputException() {
        Admin admin = new Admin();
        admin.setId("u-1");
        admin.setName("Admin");
        // email is null so findByEmail is never called

        StudentProfileDocument studentDoc = buildStudentDoc();

        when(userRepository.findById("u-1")).thenReturn(Optional.of(studentDoc));

        assertThrows(InvalidInputException.class, () -> adapter.update("u-1", admin));
    }

    // ── findAllStudents ───────────────────────────────────────────

    @Test
    void whenFindAllStudents_thenReturnsOnlyStudents() {
        StudentProfileDocument studentDoc = buildStudentDoc();

        when(userRepository.findByUserType(any())).thenReturn(List.of(studentDoc));

        List<StudentProfile> result = adapter.findAllStudents();

        assertEquals(1, result.size());
        assertInstanceOf(StudentProfile.class, result.get(0));
    }

    @Test
    void whenFindAllStudentsReturnsEmpty_thenReturnsEmptyList() {
        when(userRepository.findByUserType(any())).thenReturn(List.of());

        List<StudentProfile> result = adapter.findAllStudents();

        assertTrue(result.isEmpty());
    }
}
