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
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
        s.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        return s;
    }

    private StudentProfileDocument buildStudentDoc() {
        StudentProfileDocument doc = new StudentProfileDocument();
        doc.setId("u-1");
        doc.setName("Test User");
        doc.setEmail(VALID_EMAIL);
        doc.setGender(GenderEnum.MALE);
        doc.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        doc.setSemester(3);
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
        StudentProfileDocument savedDoc = buildStudentDoc();

        when(userRepository.save(any())).thenReturn(savedDoc);

        User result = adapter.update("u-1", student);

        assertNotNull(result);
        assertEquals("u-1", student.getId());
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
}
