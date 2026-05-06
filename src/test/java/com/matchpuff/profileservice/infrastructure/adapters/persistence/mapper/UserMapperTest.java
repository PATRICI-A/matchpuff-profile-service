package com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper;

import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.ScheduleDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.TagDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    // ─────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────

    private StudentProfile buildStudent() {
        StudentProfile s = new StudentProfile();
        s.setId("u-1");
        s.setName("Test User");
        s.setEmail("test@escuelaing.edu.co");
        s.setPasswordHash("HashedPassword123");
        s.setDateOfBirth(LocalDate.of(2000, 1, 1));
        s.setCreatedAt(LocalDateTime.now());
        s.setPhotoUrl("photo.jpg");
        s.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        s.setSemester(5);
        s.setStudentCarnet(Long.valueOf(20211000));
        s.setBiography("bio");
        s.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);

        Schedule schedule = new Schedule(
            DayOfWeekEnum.MONDAY,
            "Class",
            LocalTime.of(8, 0),
            LocalTime.of(10, 0)
        );

        Tag tag = new Tag();
        tag.setName("Java");
        tag.setCategory("TECH");

        s.setSchedules(List.of(schedule));
        s.setTags(List.of(tag));

        return s;
    }

    private StudentProfileDocument buildDocument() {
        StudentProfileDocument doc = new StudentProfileDocument();
        doc.setId("u-1");
        doc.setName("Test User");
        doc.setEmail("test@escuelaing.edu.co");
        doc.setPasswordHash("HashedPassword123");
        doc.setBirthdate(LocalDateTime.of(2000, 1, 1, 0, 0));
        doc.setCreatedAt(LocalDateTime.now());
        doc.setPhotourl("photo.jpg");
        doc.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        doc.setSemester(5);
        doc.setStudentCarnet(Long.valueOf(20211000));
        doc.setBiography("bio");
        doc.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        doc.setUserType(UserType.STUDENT);

        ScheduleDocument sd = new ScheduleDocument();
        sd.setDayOfWeek(DayOfWeekEnum.MONDAY);
        sd.setName("Class");
        sd.setStartHour(LocalTime.of(8, 0));
        sd.setFinishHour(LocalTime.of(10, 0));

        TagDocument td = new TagDocument();
        td.setName("Java");
        td.setCategory("TECH");

        doc.setSchedule(List.of(sd));
        doc.setInterests(List.of(td));

        return doc;
    }

    // ─────────────────────────────────────────────
    // toDocument
    // ─────────────────────────────────────────────

    @Test
    void givenStudent_whenToDocument_thenMapsCorrectly() {
        StudentProfile student = buildStudent();

        StudentProfileDocument result = UserMapper.toDocument(student);

        assertNotNull(result);
        assertEquals("u-1", result.getId());
        assertEquals(UserType.STUDENT, result.getUserType());
        assertEquals("Test User", result.getName());
        assertEquals("test@escuelaing.edu.co", result.getEmail());
        assertNotNull(result.getSchedule());
        assertNotNull(result.getInterests());
    }

    // ─────────────────────────────────────────────
    // toDomain
    // ─────────────────────────────────────────────

    @Test
    void givenDocument_whenToDomain_thenMapsCorrectly() {
        StudentProfileDocument doc = buildDocument();

        StudentProfile result = UserMapper.toDomain(doc);

        assertNotNull(result);
        assertEquals("u-1", result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@escuelaing.edu.co", result.getEmail());
        assertEquals(5, result.getSemester());
        assertNotNull(result.getSchedules());
        assertNotNull(result.getTags());
    }


    // ─────────────────────────────────────────────
    // toDomainList
    // ─────────────────────────────────────────────

    @Test
    void givenDocumentList_whenToDomainList_thenMapsAll() {
        List<StudentProfileDocument> docs = List.of(buildDocument());

        List<StudentProfile> result = UserMapper.toDomainList(docs);

        assertEquals(1, result.size());
    }

    @Test
    void givenNullList_whenToDomainList_thenReturnsEmpty() {
        List<StudentProfile> result = UserMapper.toDomainList(null);

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

        List<Schedule> result = UserMapper.toScheduleList(List.of(doc));

        assertEquals(1, result.size());
        assertEquals(DayOfWeekEnum.MONDAY, result.get(0).getDayOfWeek());
    }

    @Test
    void givenNullSchedules_whenToScheduleList_thenReturnsEmpty() {
        assertTrue(UserMapper.toScheduleList(null).isEmpty());
    }

    // ─────────────────────────────────────────────
    // Tag mapping
    // ─────────────────────────────────────────────

    @Test
    void givenTagDocs_whenToTagList_thenMapsCorrectly() {
        TagDocument doc = new TagDocument();
        doc.setName("Java");
        doc.setCategory("TECH");

        List<Tag> result = UserMapper.toTagList(List.of(doc));

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
    }

    @Test
    void givenNullTags_whenToTagList_thenReturnsEmpty() {
        assertTrue(UserMapper.toTagList(null).isEmpty());
    }

    // ─────────────────────────────────────────────
    // Reverse mapping
    // ─────────────────────────────────────────────

    @Test
    void givenStudent_whenToDocumentAndBack_thenConsistencyMaintained() {
        StudentProfile original = buildStudent();

        StudentProfileDocument doc = UserMapper.toDocument(original);
        StudentProfile mappedBack = UserMapper.toDomain(doc);

        assertEquals(original.getId(), mappedBack.getId());
        assertEquals(original.getEmail(), mappedBack.getEmail());
        assertEquals(original.getName(), mappedBack.getName());
    }
}