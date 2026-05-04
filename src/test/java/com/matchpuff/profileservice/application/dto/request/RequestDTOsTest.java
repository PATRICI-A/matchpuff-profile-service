package com.matchpuff.profileservice.application.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.model.Schedule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Request DTOs Tests")
class RequestDTOsTest {

    @Test
    void givenScheduleRequest_whenCreated_thenFieldsCanBeSet() {
        // Given
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        String name = "Algorithms";
        DayOfWeekEnum dayOfWeek = DayOfWeekEnum.MONDAY;
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        // When
        scheduleRequest.setName(name);
        scheduleRequest.setDayOfWeek(dayOfWeek);
        scheduleRequest.setStartTime(startTime);
        scheduleRequest.setEndTime(endTime);

        // Then
        assertEquals(name, scheduleRequest.getName());
        assertEquals(dayOfWeek, scheduleRequest.getDayOfWeek());
        assertEquals(startTime, scheduleRequest.getStartTime());
        assertEquals(endTime, scheduleRequest.getEndTime());
    }

    @Test
    void givenTagRequest_whenCreated_thenFieldsCanBeSet() {
        // Given
        TagRequest tagRequest = new TagRequest();
        String name = "Programming";
        String category = "Skills";

        // When
        tagRequest.setName(name);
        tagRequest.setCategory(category);

        // Then
        assertEquals(name, tagRequest.getName());
        assertEquals(category, tagRequest.getCategory());
    }

    @Test
    void givenUpdateUserRequest_whenCreated_thenFieldsCanBeSet() {
        // Given
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        String name = "Updated Name";
        String biography = "Updated biography";
        PrivacyLevelEnum privacyLevel = PrivacyLevelEnum.PUBLIC;

        // When
        updateRequest.setName(name);
        updateRequest.setBiography(biography);
        updateRequest.setPrivacyLevel(privacyLevel);

        // Then
        assertEquals(name, updateRequest.getName());
        assertEquals(biography, updateRequest.getBiography());
        assertEquals(privacyLevel, updateRequest.getPrivacyLevel());
    }

    @Test
    void givenTagsUpdateRequest_whenCreated_thenTagsCanBeSet() {
        // Given
        TagsUpdateRequest tagsUpdateRequest = new TagsUpdateRequest();
        List<TagRequest> tags = new ArrayList<>();
        TagRequest tag1 = new TagRequest();
        tag1.setName("Java");
        tag1.setCategory("Programming");
        tags.add(tag1);

        // When
        tagsUpdateRequest.setTags(tags);

        // Then
        assertEquals(1, tagsUpdateRequest.getTags().size());
        assertEquals("Java", tagsUpdateRequest.getTags().get(0).getName());
    }

    @Test
    void givenScheduleUpdateRequest_whenCreated_thenSchedulesCanBeSet() {
        // Given
        ScheduleUpdateRequest scheduleUpdateRequest = new ScheduleUpdateRequest();
        Schedule schedule = new Schedule(DayOfWeekEnum.TUESDAY, "Math", LocalTime.of(10, 0), LocalTime.of(12, 0));
        List<Schedule> schedules = new ArrayList<>();
        schedules.add(schedule);

        // When
        scheduleUpdateRequest.setSchedules(schedules);

        // Then
        assertEquals(1, scheduleUpdateRequest.getSchedules().size());
        assertEquals("Math", scheduleUpdateRequest.getSchedules().get(0).getName());
    }

    @Test
    void givenUserOrganizerRequest_whenCreated_thenFieldsCanBeSet() {
        // Given
        UserOrganizerRequest organizerRequest = new UserOrganizerRequest();
        String name = "John Organizer";
        String email = "john@organizers.com";
        String contactInfo = "3005551234";

        // When
        organizerRequest.setName(name);
        organizerRequest.setEmail(email);
        organizerRequest.setContactInfo(contactInfo);

        // Then
        assertEquals(name, organizerRequest.getName());
        assertEquals(email, organizerRequest.getEmail());
        assertEquals(contactInfo, organizerRequest.getContactInfo());
    }

    @Test
    void givenUserAdminRequest_whenCreated_thenFieldsCanBeSet() {
        // Given
        UserAdminRequest adminRequest = new UserAdminRequest();
        String name = "Admin User";
        String email = "admin@company.com";

        // When
        adminRequest.setName(name);
        adminRequest.setEmail(email);

        // Then
        assertEquals(name, adminRequest.getName());
        assertEquals(email, adminRequest.getEmail());
    }

    @Test
    void givenMultipleScheduleRequests_whenCreated_thenEachHasOwnState() {
        // When
        ScheduleRequest schedule1 = new ScheduleRequest();
        schedule1.setName("Class 1");

        ScheduleRequest schedule2 = new ScheduleRequest();
        schedule2.setName("Class 2");

        // Then
        assertEquals("Class 1", schedule1.getName());
        assertEquals("Class 2", schedule2.getName());
        assertNotEquals(schedule1.getName(), schedule2.getName());
    }

    @Test
    void givenUpdateUserRequest_withAllFields_whenSet_thenAllFieldsAreRetrievable() {
        // Given
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        String name = "New Name";
        String biography = "New bio";
        String photo = "http://photo.url";
        PrivacyLevelEnum privacy = PrivacyLevelEnum.PRIVATE;
        List<String> interests = new ArrayList<>();
        interests.add("programming");

        // When
        updateRequest.setName(name);
        updateRequest.setBiography(biography);
        updateRequest.setPhoto(photo);
        updateRequest.setPrivacyLevel(privacy);
        updateRequest.setInterests(interests);

        // Then
        assertEquals(name, updateRequest.getName());
        assertEquals(biography, updateRequest.getBiography());
        assertEquals(photo, updateRequest.getPhoto());
        assertEquals(privacy, updateRequest.getPrivacyLevel());
        assertEquals(1, updateRequest.getInterests().size());
    }

    @Test
    void givenScheduleRequest_withFullDay_whenCreated_thenTimesAreCorrect() {
        // Given
        ScheduleRequest schedule = new ScheduleRequest();
        LocalTime morning = LocalTime.of(7, 30);
        LocalTime evening = LocalTime.of(19, 0);

        // When
        schedule.setStartTime(morning);
        schedule.setEndTime(evening);

        // Then
        assertEquals(morning, schedule.getStartTime());
        assertEquals(evening, schedule.getEndTime());
        assertTrue(schedule.getEndTime().isAfter(schedule.getStartTime()));
    }
}
