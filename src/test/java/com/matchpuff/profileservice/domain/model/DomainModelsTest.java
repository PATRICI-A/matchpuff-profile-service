package com.matchpuff.profileservice.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@DisplayName("Domain Models Tests")
class DomainModelsTest {

    @Test
    void givenAdmin_whenCreated_thenCanSetAndGetFields() {
        // Given
        Admin admin = new Admin();

        // When
        admin.setId(UUID.randomUUID());
        admin.setName("Administrator");
        admin.setEmail("admin@escuelaing.edu.co");
        admin.setGender(GenderEnum.MALE);
        admin.setCreatedAt(LocalDateTime.now());

        // Then
        assertNotNull(admin.getId());
        assertEquals("Administrator", admin.getName());
        assertEquals("admin@escuelaing.edu.co", admin.getEmail());
        assertEquals(GenderEnum.MALE, admin.getGender());
        assertNotNull(admin.getCreatedAt());
    }

    @Test
    void givenOrganizer_whenCreated_thenCanSetContactInfo() {
        // Given
        Organizer organizer = new Organizer();

        // When
        organizer.setId(UUID.randomUUID());
        organizer.setName("Event Organizer");
        organizer.setEmail("organizer@escuelaing.edu.co");
        organizer.setGender(GenderEnum.FEMALE);
        organizer.setContactInfo("3001234567");

        // Then
        assertNotNull(organizer.getId());
        assertEquals("Event Organizer", organizer.getName());
        assertEquals("organizer@escuelaing.edu.co", organizer.getEmail());
        assertEquals(GenderEnum.FEMALE, organizer.getGender());
        assertEquals("3001234567", organizer.getContactInfo());
    }

    @Test
    void givenSchedule_whenCreated_thenAllFieldsAreSet() {
        // Given
        DayOfWeekEnum dayOfWeek = DayOfWeekEnum.WEDNESDAY;
        String name = "Database Class";
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        // When
        Schedule schedule = new Schedule(dayOfWeek, name, startTime, endTime);

        // Then
        assertEquals(dayOfWeek, schedule.getDayOfWeek());
        assertEquals(name, schedule.getName());
        assertEquals(startTime, schedule.getStartTime());
        assertEquals(endTime, schedule.getEndTime());
    }

    @Test
    void givenSchedule_whenSetters_thenFieldsAreUpdated() {
        // Given
        Schedule schedule = new Schedule(DayOfWeekEnum.MONDAY, "Class", LocalTime.of(8, 0), LocalTime.of(10, 0));

        // When
        schedule.setDayOfWeek(DayOfWeekEnum.FRIDAY);
        schedule.setName("Updated Class");
        schedule.setStartTime(LocalTime.of(14, 0));
        schedule.setEndTime(LocalTime.of(16, 0));

        // Then
        assertEquals(DayOfWeekEnum.FRIDAY, schedule.getDayOfWeek());
        assertEquals("Updated Class", schedule.getName());
        assertEquals(LocalTime.of(14, 0), schedule.getStartTime());
        assertEquals(LocalTime.of(16, 0), schedule.getEndTime());
    }

    @Test
    void givenTag_whenCreated_thenCanSetNameAndCategory() {
        // Given
        Tag tag = new Tag("Java", "Programming Languages");

        // Then
        assertEquals("Java", tag.getName());
        assertEquals("Programming Languages", tag.getCategory());
    }

    @Test
    void givenMultipleTags_whenCreated_thenEachHasOwnState() {
        // When
        Tag tag1 = new Tag("Python", "Programming");
        Tag tag2 = new Tag("Leadership", "Soft Skills");

        // Then
        assertEquals("Python", tag1.getName());
        assertEquals("Leadership", tag2.getName());
        assertNotEquals(tag1.getName(), tag2.getName());
    }

    @Test
    void givenStudentProfile_whenSetDateOfBirth_thenCanRetrieveIt() {
        // Given
        StudentProfile student = new StudentProfile();
        LocalDate dateOfBirth = LocalDate.of(2000, 1, 15);

        // When
        student.setDateOfBirth(dateOfBirth);

        // Then
        assertEquals(dateOfBirth, student.getDateOfBirth());
    }

    @Test
    void givenUser_whenSetCreatedAt_thenTimeStampIsStored() {
        // Given
        User user = new Admin();
        LocalDateTime now = LocalDateTime.now();

        // When
        user.setCreatedAt(now);

        // Then
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void givenAdmin_whenNoContactInfo_thenContactInfoFieldDoesntExist() {
        // Given
        Admin admin = new Admin();

        // When - Admin doesn't have contactInfo field
        admin.setId(UUID.randomUUID());
        admin.setName("Admin");

        // Then
        assertNotNull(admin.getId());
        assertEquals("Admin", admin.getName());
        // Verify that Admin doesn't have contactInfo
        assertTrue(!java.util.Arrays.stream(admin.getClass().getDeclaredFields())
                .anyMatch(f -> f.getName().equals("contactInfo")));
    }

    @Test
    void givenOrganizer_whenHasContactInfo_thenContactInfoExists() {
        // Given
        Organizer organizer = new Organizer();

        // When
        organizer.setContactInfo("contact@example.com");

        // Then
        assertEquals("contact@example.com", organizer.getContactInfo());
        // Verify that Organizer has contactInfo
        assertTrue(java.util.Arrays.stream(organizer.getClass().getDeclaredFields())
                .anyMatch(f -> f.getName().equals("contactInfo")));
    }

    @Test
    void givenScheduleWithDifferentDays_whenCreated_thenEachDayIsPreserved() {
        // When
        Schedule monday = new Schedule(DayOfWeekEnum.MONDAY, "Class", LocalTime.of(8, 0), LocalTime.of(10, 0));
        Schedule friday = new Schedule(DayOfWeekEnum.FRIDAY, "Class", LocalTime.of(8, 0), LocalTime.of(10, 0));

        // Then
        assertEquals(DayOfWeekEnum.MONDAY, monday.getDayOfWeek());
        assertEquals(DayOfWeekEnum.FRIDAY, friday.getDayOfWeek());
        assertNotEquals(monday.getDayOfWeek(), friday.getDayOfWeek());
    }

    @Test
    void givenSchedule_whenDurationIsMultipleHours_thenTimesAreCorrect() {
        // When
        Schedule schedule = new Schedule(
            DayOfWeekEnum.TUESDAY,
            "Long Class",
            LocalTime.of(8, 30),
            LocalTime.of(16, 45)
        );

        // Then
        assertTrue(schedule.getEndTime().isAfter(schedule.getStartTime()));
        assertEquals(LocalTime.of(8, 30), schedule.getStartTime());
        assertEquals(LocalTime.of(16, 45), schedule.getEndTime());
    }
}
