package com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper;

import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.ScheduleDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UserMapperTest {

    private final UserPersistenceMapper mapper = Mappers.getMapper(UserPersistenceMapper.class);

    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────

    private StudentProfile buildStudent() {
        StudentProfile s = new StudentProfile();
        s.setId(UUID.randomUUID());
        s.setName("Test User");
        s.setEmail("test@escuelaing.edu.co");
        s.setPasswordHash("HashedPassword123");
        s.setGender(GenderEnum.MALE);
        s.setDateOfBirth(LocalDate.of(2000, 1, 1));
        s.setCreatedAt(LocalDateTime.now());
        s.setPhotoUrl("photo.jpg");
        s.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        s.setSemester(5);
        s.setStudentCarnet("2021100011");
        s.setBiography("bio");
        s.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);

        Schedule schedule = new Schedule(
            DayOfWeekEnum.MONDAY,
            "Class",
            LocalTime.of(8, 0),
            LocalTime.of(10, 0)
        );
        s.setSchedulesAvailability(List.of(schedule));
        s.setTagsId(List.of(UUID.randomUUID()));

        return s;
    }

    private StudentProfileDocument buildDocument() {
        StudentProfileDocument doc = new StudentProfileDocument();
        doc.setId(UUID.randomUUID());
        doc.setName("Test User");
        doc.setEmail("test@escuelaing.edu.co");
        doc.setPasswordHash("HashedPassword123");
        doc.setGender(GenderEnum.MALE);
        doc.setBirthdate(LocalDateTime.of(2000, 1, 1, 0, 0));
        doc.setCreatedAt(LocalDateTime.now());
        doc.setPhotourl("photo.jpg");
        doc.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        doc.setSemester(5);
        doc.setStudentCarnet("2021100011");
        doc.setBiography("bio");
        doc.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        doc.setUserType(UserType.STUDENT);

        ScheduleDocument sd = new ScheduleDocument();
        sd.setDayOfWeek(DayOfWeekEnum.MONDAY);
        sd.setName("Class");
        sd.setStartHour(LocalTime.of(8, 0));
        sd.setFinishHour(LocalTime.of(10, 0));

        doc.setScheduleAvailability(List.of(sd));
        doc.setTagsId(List.of(UUID.randomUUID()));

        return doc;
    }

    // ─────────────────────────────────────────────
    // toDocument
    // ─────────────────────────────────────────────

    @Test
    void givenStudent_whenToDocument_thenMapsCorrectly() {
        StudentProfile student = buildStudent();

        StudentProfileDocument result = mapper.toDocument(student);

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        assertEquals(UserType.STUDENT, result.getUserType());
        assertEquals("Test User", result.getName());
        assertEquals("test@escuelaing.edu.co", result.getEmail());
        assertNotNull(result.getScheduleAvailability());
        assertNotNull(result.getTagsId());
    }

    // ─────────────────────────────────────────────
    // toDomain
    // ─────────────────────────────────────────────

    @Test
    void givenDocument_whenToDomain_thenMapsCorrectly() {
        StudentProfileDocument doc = buildDocument();

        StudentProfile result = mapper.toDomain(doc);

        assertNotNull(result);
        assertEquals(doc.getId(), result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@escuelaing.edu.co", result.getEmail());
        assertEquals(5, result.getSemester());
        assertNotNull(result.getSchedulesAvailability());
        assertNotNull(result.getTagsId());
    }


    // ─────────────────────────────────────────────
    // toDomainList
    // ─────────────────────────────────────────────

    @Test
    void givenDocumentList_whenToDomainList_thenMapsAll() {
        List<StudentProfileDocument> docs = List.of(buildDocument());

        List<StudentProfile> result = mapper.toDomainList(docs);

        assertEquals(1, result.size());
    }

    @Test
    void givenNullList_whenToDomainList_thenReturnsEmpty() {
        List<StudentProfile> result = mapper.toDomainList(null);

        assertTrue(result.isEmpty());
    }

    // ─────────────────────────────────────────────
    // Schedule mapping
    // ─────────────────────────────────────────────

    @Test
    void givenScheduleDocs_whenToScheduleList_thenMapsCorrectly() {
        ScheduleDocument doc = new ScheduleDocument();
        doc.setDayOfWeek(DayOfWeekEnum.MONDAY);
        doc.setName("Class");
        doc.setStartHour(LocalTime.of(8, 0));
        doc.setFinishHour(LocalTime.of(10, 0));

        List<Schedule> result = mapper.toScheduleList(List.of(doc));

        assertEquals(1, result.size());
        assertEquals(DayOfWeekEnum.MONDAY, result.get(0).getDayOfWeek());
    }

    @Test
    void givenNullSchedules_whenToScheduleList_thenReturnsEmpty() {
        assertTrue(mapper.toScheduleList(null).isEmpty());
    }

    // ─────────────────────────────────────────────
    // toDocument – null date / null collections
    // ─────────────────────────────────────────────

    @Test
    void givenStudentWithNullDateOfBirth_whenToDocument_thenBirthdateIsNull() {
        StudentProfile student = buildStudent();
        student.setDateOfBirth(null);

        StudentProfileDocument result = mapper.toDocument(student);

        assertNotNull(result);
        assertNull(result.getBirthdate());
    }

    @Test
    void givenStudentWithNullSchedules_whenToDocument_thenScheduleIsEmpty() {
        StudentProfile student = buildStudent();
        student.setSchedulesAvailability(null);

        StudentProfileDocument result = mapper.toDocument(student);

        assertNotNull(result);
        assertNull(result.getScheduleAvailability());
    }

    @Test
    void givenStudentWithNullTags_whenToDocument_thenInterestsIsEmpty() {
        StudentProfile student = buildStudent();
        student.setTagsId(null);

        StudentProfileDocument result = mapper.toDocument(student);

        assertNotNull(result);
        assertNull(result.getTagsId());
    }

    // ─────────────────────────────────────────────
    // toDomain – null date / null semester
    // ─────────────────────────────────────────────

    @Test
    void givenDocumentWithNullBirthdate_whenToDomain_thenDateOfBirthIsNull() {
        StudentProfileDocument doc = buildDocument();
        doc.setBirthdate(null);

        StudentProfile result = mapper.toDomain(doc);

        assertNotNull(result);
        assertNull(result.getDateOfBirth());
    }

    @Test
    void givenDocumentWithNullSemester_whenToDomain_thenSemesterIsZero() {
        StudentProfileDocument doc = buildDocument();
        doc.setSemester(null);

        StudentProfile result = mapper.toDomain(doc);

        assertNotNull(result);
        assertEquals(0, result.getSemester());
    }

    // ─────────────────────────────────────────────
    // Reverse mapping
    // ─────────────────────────────────────────────

    @Test
    void givenStudent_whenToDocumentAndBack_thenConsistencyMaintained() {
        StudentProfile original = buildStudent();

        StudentProfileDocument doc = mapper.toDocument(original);
        StudentProfile mappedBack = mapper.toDomain(doc);

        assertEquals(original.getId(), mappedBack.getId());
        assertEquals(original.getEmail(), mappedBack.getEmail());
        assertEquals(original.getName(), mappedBack.getName());
    }

    // ─────────────────────────────────────────────
    // Admin mapping
    // ─────────────────────────────────────────────

    @Test
    void givenAdmin_whenToDocument_thenMapsCorrectly() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setName("Admin User");
        admin.setEmail("admin@escuelaing.edu.co");
        admin.setPasswordHash("HashedPass123");
        admin.setGender(GenderEnum.MALE);

        AdminProfileDocument result = mapper.toDocument(admin);

        assertNotNull(result);
        assertEquals(admin.getId(), result.getId());
        assertEquals(UserType.ADMIN, result.getUserType());
        assertEquals("Admin User", result.getName());
    }

    @Test
    void givenNullAdmin_whenToDocument_thenReturnsNull() {
        assertNull(mapper.toDocument((Admin) null));
    }

    @Test
    void givenAdminDocument_whenToDomain_thenMapsCorrectly() {
        AdminProfileDocument doc = new AdminProfileDocument();
        doc.setId(UUID.randomUUID());
        doc.setName("Admin User");
        doc.setEmail("admin@escuelaing.edu.co");
        doc.setPasswordHash("HashedPass123");
        doc.setGender(GenderEnum.MALE);

        Admin result = mapper.toDomain(doc);

        assertNotNull(result);
        assertEquals(doc.getId(), result.getId());
        assertEquals("Admin User", result.getName());
        assertEquals(GenderEnum.MALE, result.getGender());
    }

    @Test
    void givenNullAdminDocument_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((AdminProfileDocument) null));
    }

    // ─────────────────────────────────────────────
    // Organizer mapping
    // ─────────────────────────────────────────────

    @Test
    void givenOrganizer_whenToDocument_thenMapsCorrectly() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Club Org");
        organizer.setEmail("org@escuelaing.edu.co");
        organizer.setPasswordHash("HashedPass123");
        organizer.setGender(GenderEnum.FEMALE);
        organizer.setContactInfo("contact@evento.co");

        OrganizerProfileDocument result = mapper.toDocument(organizer);

        assertNotNull(result);
        assertEquals(organizer.getId(), result.getId());
        assertEquals(UserType.ORGANIZER, result.getUserType());
        assertEquals("contact@evento.co", result.getContact());
    }

    @Test
    void givenNullOrganizer_whenToDocument_thenReturnsNull() {
        assertNull(mapper.toDocument((Organizer) null));
    }

    @Test
    void givenOrganizerDocument_whenToDomain_thenMapsCorrectly() {
        OrganizerProfileDocument doc = new OrganizerProfileDocument();
        doc.setId(UUID.randomUUID());
        doc.setName("Club Org");
        doc.setEmail("org@escuelaing.edu.co");
        doc.setGender(GenderEnum.FEMALE);
        doc.setContact("contact@evento.co");

        Organizer result = mapper.toDomain(doc);

        assertNotNull(result);
        assertEquals(doc.getId(), result.getId());
        assertEquals("contact@evento.co", result.getContactInfo());
    }

    @Test
    void givenNullOrganizerDocument_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((OrganizerProfileDocument) null));
    }

    // ─────────────────────────────────────────────
    // toDomainByType
    // ─────────────────────────────────────────────

    @Test
    void givenNullDocument_whenToDomainByType_thenReturnsEmpty() {
        Optional<User> result = mapper.toDomainByType(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenDocumentWithNullType_whenToDomainByType_thenReturnsEmpty() {
        UserDocument mockDoc = mock(UserDocument.class);

        Optional<User> result = mapper.toDomainByType(mockDoc);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenAdminDocument_whenToDomainByType_thenReturnsAdmin() {
        AdminProfileDocument doc = new AdminProfileDocument();
        doc.setId(UUID.randomUUID());
        doc.setName("Admin");
        doc.setEmail("admin@escuelaing.edu.co");
        doc.setGender(GenderEnum.MALE);

        Optional<User> result = mapper.toDomainByType(doc);

        assertTrue(result.isPresent());
        assertInstanceOf(Admin.class, result.get());
    }

    @Test
    void givenOrganizerDocument_whenToDomainByType_thenReturnsOrganizer() {
        OrganizerProfileDocument doc = new OrganizerProfileDocument();
        doc.setId(UUID.randomUUID());
        doc.setName("Organizer");
        doc.setEmail("org@escuelaing.edu.co");
        doc.setGender(GenderEnum.MALE);
        doc.setContact("contact@evento.co");

        Optional<User> result = mapper.toDomainByType(doc);

        assertTrue(result.isPresent());
        assertInstanceOf(Organizer.class, result.get());
    }

    // ─────────────────────────────────────────────
    // Student toDocument with null birthdate
    // ─────────────────────────────────────────────

    @Test
    void givenStudentWithNullBirthdate_whenToDocument_thenBirthdateIsNull() {
        StudentProfile student = buildStudent();
        student.setDateOfBirth(null);

        StudentProfileDocument result = mapper.toDocument(student);

        assertNull(result.getBirthdate());
    }

    @Test
    void givenNullStudent_whenToDocument_thenReturnsNull() {
        assertNull(mapper.toDocument((StudentProfile) null));
    }
}
