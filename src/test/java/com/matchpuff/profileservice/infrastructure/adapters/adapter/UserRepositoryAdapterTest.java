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
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        s.setId(UUID.randomUUID());
        s.setName("Test User");
        s.setEmail(VALID_EMAIL);
        s.setGender(GenderEnum.MALE);
        s.setDateOfBirth(LocalDate.of(2000, 1, 1));
        s.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        s.setSemester(3);
        s.setStudentCarnet("2021100011");
        s.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        return s;
    }

    private StudentProfileDocument buildStudentDoc() {
        StudentProfileDocument doc = new StudentProfileDocument();
        doc.setId(UUID.randomUUID());
        doc.setName("Test User");
        doc.setEmail(VALID_EMAIL);
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.MALE);
        doc.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        doc.setSemester(3);
        doc.setStudentCarnet("2021100011");
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
    void givenNewStudent_whenSave_thenSetsCreatedAtBeforePersisting() {
        StudentProfile student = buildStudent();
        student.setCreatedAt(null);

        StudentProfileDocument savedDoc = buildStudentDoc();
        savedDoc.setCreatedAt(null);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(savedDoc);

        adapter.save(student);

        ArgumentCaptor<StudentProfileDocument> captor = ArgumentCaptor.forClass(StudentProfileDocument.class);
        verify(userRepository).save(captor.capture());
        assertNotNull(captor.getValue().getCreatedAt());
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
        UUID studentId = UUID.randomUUID();
        doc.setId(studentId);

        when(userRepository.findById(studentId)).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findById(studentId.toString());

        assertTrue(result.isPresent());
        assertInstanceOf(StudentProfile.class, result.get());
        assertEquals(VALID_EMAIL, result.get().getEmail());
    }

    @Test
    void givenExistingAdminId_whenFindById_thenReturnsAdmin() {
        AdminProfileDocument doc = new AdminProfileDocument();
        UUID adminId = UUID.randomUUID();
        doc.setId(adminId);
        doc.setName("Admin");
        doc.setEmail(VALID_EMAIL);
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.MALE);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findById(adminId.toString());

        assertTrue(result.isPresent());
        assertInstanceOf(Admin.class, result.get());
    }

    @Test
    void givenExistingOrganizerId_whenFindById_thenReturnsOrganizer() {
        OrganizerProfileDocument doc = new OrganizerProfileDocument();
        UUID organizerId = UUID.randomUUID();
        doc.setId(organizerId);
        doc.setName("Organizer");
        doc.setEmail(VALID_EMAIL);
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.FEMALE);
        doc.setContact("contacto@evento.co");

        when(userRepository.findById(organizerId)).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findById(organizerId.toString());

        assertTrue(result.isPresent());
        assertInstanceOf(Organizer.class, result.get());
        assertEquals("contacto@evento.co", ((Organizer) result.get()).getContactInfo());
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
        UUID currentId = UUID.randomUUID();

        currentDoc.setId(currentId);
        savedDoc.setId(currentId);
        student.setId(UUID.randomUUID());

        when(userRepository.findById(currentId)).thenReturn(Optional.of(currentDoc));
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update(currentId.toString(), student);

        assertNotNull(result);
        assertEquals(currentId, result.getId());
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
        adminDoc.setId(UUID.randomUUID());
        adminDoc.setName("Admin");
        adminDoc.setEmail(VALID_EMAIL);

        OrganizerProfileDocument orgDoc = new OrganizerProfileDocument();
        orgDoc.setId(UUID.randomUUID());
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
        admin.setId(UUID.randomUUID());
        admin.setName("Root Admin");
        admin.setEmail(VALID_EMAIL);
        admin.setGender(GenderEnum.MALE);

        AdminProfileDocument savedDoc = new AdminProfileDocument();
        savedDoc.setId(UUID.randomUUID());
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
        organizer.setId(UUID.randomUUID());
        organizer.setName("Club Org");
        organizer.setEmail(VALID_EMAIL);
        organizer.setGender(GenderEnum.FEMALE);
        organizer.setContactInfo("club@evento.co");

        OrganizerProfileDocument savedDoc = new OrganizerProfileDocument();
        savedDoc.setId(UUID.randomUUID());
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
        UUID userId = UUID.randomUUID();

        adapter.delete(userId.toString());

        verify(userRepository).deleteById(userId);
    }

    // ── update Admin ──────────────────────────────────────────────

    @Test
    void givenAdminProfile_whenUpdate_thenReturnsUpdatedAdmin() {
        Admin admin = new Admin();
        UUID adminId = UUID.randomUUID();
        admin.setId(adminId);
        admin.setName("Updated Admin");
        admin.setEmail(VALID_EMAIL);
        admin.setGender(GenderEnum.MALE);

        AdminProfileDocument currentDoc = new AdminProfileDocument();
        currentDoc.setId(adminId);
        currentDoc.setName("Old Admin");
        currentDoc.setEmail(VALID_EMAIL);
        currentDoc.setGender(GenderEnum.MALE);

        AdminProfileDocument savedDoc = new AdminProfileDocument();
        savedDoc.setId(adminId);
        savedDoc.setName("Updated Admin");
        savedDoc.setEmail(VALID_EMAIL);

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(currentDoc));
        when(userRepository.findById(adminId)).thenReturn(Optional.of(currentDoc));
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update(adminId.toString(), admin);

        assertNotNull(result);
        assertInstanceOf(Admin.class, result);
        verify(userRepository).save(any());
    }

    // ── update Organizer ──────────────────────────────────────────

    @Test
    void givenOrganizerProfile_whenUpdate_thenReturnsUpdatedOrganizer() {
        Organizer organizer = new Organizer();
        UUID organizerId = UUID.randomUUID();
        organizer.setId(organizerId);
        organizer.setName("Updated Org");
        organizer.setEmail(VALID_EMAIL);
        organizer.setGender(GenderEnum.FEMALE);
        organizer.setContactInfo("new@evento.co");

        OrganizerProfileDocument currentDoc = new OrganizerProfileDocument();
        currentDoc.setId(organizerId);
        currentDoc.setName("Old Org");
        currentDoc.setEmail(VALID_EMAIL);
        currentDoc.setContact("old@evento.co");

        OrganizerProfileDocument savedDoc = new OrganizerProfileDocument();
        savedDoc.setId(organizerId);
        savedDoc.setName("Updated Org");
        savedDoc.setEmail(VALID_EMAIL);
        savedDoc.setContact("new@evento.co");

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(currentDoc));
        when(userRepository.findById(organizerId)).thenReturn(Optional.of(currentDoc));
        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update(organizerId.toString(), organizer);

        assertNotNull(result);
        assertInstanceOf(Organizer.class, result);
        verify(userRepository).save(any());
    }

    // ── update email conflict ─────────────────────────────────────

    @Test
    void givenEmailBelongsToAnotherUser_whenUpdate_thenThrowsInvalidInputException() {
        StudentProfile student = buildStudent();
        student.setEmail("other@escuelaing.edu.co");
        UUID userId = UUID.randomUUID();

        StudentProfileDocument conflictDoc = buildStudentDoc();
        conflictDoc.setId(UUID.randomUUID());
        conflictDoc.setEmail("other@escuelaing.edu.co");

        when(userRepository.findByEmail("other@escuelaing.edu.co")).thenReturn(Optional.of(conflictDoc));

        assertThrows(InvalidInputException.class, () -> adapter.update(userId.toString(), student));
    }

    // ── update type mismatch ──────────────────────────────────────

    @Test
    void givenStudentDocumentButAdminRequest_whenUpdate_thenThrowsInvalidInputException() {
        Admin admin = new Admin();
        UUID adminId = UUID.randomUUID();
        admin.setId(adminId);
        admin.setName("Admin");
        // email is null so findByEmail is never called

        StudentProfileDocument studentDoc = buildStudentDoc();

        when(userRepository.findById(adminId)).thenReturn(Optional.of(studentDoc));

        assertThrows(InvalidInputException.class, () -> adapter.update(adminId.toString(), admin));
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
