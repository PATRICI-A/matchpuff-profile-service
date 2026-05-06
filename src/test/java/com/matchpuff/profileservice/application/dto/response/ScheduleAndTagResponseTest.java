package com.matchpuff.profileservice.application.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;

import java.time.LocalTime;

@DisplayName("ScheduleResponse and TagResponse Tests")
class ScheduleAndTagResponseTest {

    private ScheduleResponse scheduleResponse;
    private TagResponse tagResponse;

    @BeforeEach
    void setUp() {
        scheduleResponse = new ScheduleResponse();
        tagResponse = new TagResponse();
    }

    @Test
    void givenScheduleResponse_whenSetters_thenAllFieldsAreSet() {
        // Given
        DayOfWeekEnum dayOfWeek = DayOfWeekEnum.MONDAY;
        String name = "Algorithms";
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        // When
        scheduleResponse.setDayOfWeek(dayOfWeek);
        scheduleResponse.setName(name);
        scheduleResponse.setStartTime(startTime);
        scheduleResponse.setEndTime(endTime);

        // Then
        assertEquals(dayOfWeek, scheduleResponse.getDayOfWeek());
        assertEquals(name, scheduleResponse.getName());
        assertEquals(startTime, scheduleResponse.getStartTime());
        assertEquals(endTime, scheduleResponse.getEndTime());
    }

    @Test
    void givenTagResponse_whenSetters_thenFieldsAreSet() {
        // Given
        String name = "Java";
        String category = "Programming";

        // When
        tagResponse.setName(name);
        tagResponse.setCategory(category);

        // Then
        assertEquals(name, tagResponse.getName());
        assertEquals(category, tagResponse.getCategory());
    }

    @Test
    void givenMultipleScheduleResponses_whenCreated_thenEachHasOwnState() {
        // When
        ScheduleResponse schedule1 = new ScheduleResponse();
        schedule1.setName("Class 1");
        schedule1.setDayOfWeek(DayOfWeekEnum.MONDAY);

        ScheduleResponse schedule2 = new ScheduleResponse();
        schedule2.setName("Class 2");
        schedule2.setDayOfWeek(DayOfWeekEnum.FRIDAY);

        // Then
        assertEquals("Class 1", schedule1.getName());
        assertEquals("Class 2", schedule2.getName());
        assertEquals(DayOfWeekEnum.MONDAY, schedule1.getDayOfWeek());
        assertEquals(DayOfWeekEnum.FRIDAY, schedule2.getDayOfWeek());
    }

    @Test
    void givenScheduleResponse_withCompleteData_thenAllDataRetrievable() {
        // Given
        LocalTime morningStart = LocalTime.of(7, 30);
        LocalTime morningEnd = LocalTime.of(9, 30);

        // When
        scheduleResponse.setDayOfWeek(DayOfWeekEnum.WEDNESDAY);
        scheduleResponse.setName("Morning Session");
        scheduleResponse.setStartTime(morningStart);
        scheduleResponse.setEndTime(morningEnd);

        // Then
        assertAll(
            () -> assertEquals(DayOfWeekEnum.WEDNESDAY, scheduleResponse.getDayOfWeek()),
            () -> assertEquals("Morning Session", scheduleResponse.getName()),
            () -> assertEquals(morningStart, scheduleResponse.getStartTime()),
            () -> assertEquals(morningEnd, scheduleResponse.getEndTime())
        );
    }

    @Test
    void givenTagResponse_withMultipleTags_thenEachRetainsIndependence() {
        // When
        TagResponse tag1 = new TagResponse();
        tag1.setName("Python");
        tag1.setCategory("Languages");

        TagResponse tag2 = new TagResponse();
        tag2.setName("Leadership");
        tag2.setCategory("Skills");

        // Then
        assertEquals("Python", tag1.getName());
        assertEquals("Leadership", tag2.getName());
        assertEquals("Languages", tag1.getCategory());
        assertEquals("Skills", tag2.getCategory());
    }

    @Test
    void givenScheduleResponse_whenUpdateName_thenNameIsModified() {
        // Given
        scheduleResponse.setName("Original Name");

        // When
        scheduleResponse.setName("Modified Name");

        // Then
        assertEquals("Modified Name", scheduleResponse.getName());
    }

    @Test
    void givenScheduleResponse_withAllDaysOfWeek_thenEachDayCanBeSet() {
        // When & Then
        for (DayOfWeekEnum day : DayOfWeekEnum.values()) {
            scheduleResponse.setDayOfWeek(day);
            assertEquals(day, scheduleResponse.getDayOfWeek());
        }
    }

    @Test
    void givenScheduleResponse_withDifferentTimes_thenTimesAreIndependent() {
        // When
        ScheduleResponse morningSchedule = new ScheduleResponse();
        morningSchedule.setStartTime(LocalTime.of(8, 0));
        morningSchedule.setEndTime(LocalTime.of(12, 0));

        ScheduleResponse afternoonSchedule = new ScheduleResponse();
        afternoonSchedule.setStartTime(LocalTime.of(14, 0));
        afternoonSchedule.setEndTime(LocalTime.of(18, 0));

        // Then
        assertTrue(morningSchedule.getEndTime().isBefore(afternoonSchedule.getStartTime()));
    }

    @Test
    void givenTagResponse_withNullValues_thenNullsAreAllowed() {
        // When
        tagResponse.setName(null);
        tagResponse.setCategory(null);

        // Then
        assertNull(tagResponse.getName());
        assertNull(tagResponse.getCategory());
    }

    @Test
    void givenScheduleResponse_withLongDuration_thenEndTimeAfterStartTime() {
        // When
        scheduleResponse.setStartTime(LocalTime.of(8, 0));
        scheduleResponse.setEndTime(LocalTime.of(20, 0));

        // Then
        assertTrue(scheduleResponse.getEndTime().isAfter(scheduleResponse.getStartTime()));
        assertEquals(12, scheduleResponse.getEndTime().getHour() - scheduleResponse.getStartTime().getHour());
    }
}
