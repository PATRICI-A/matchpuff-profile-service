package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRestMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    // ── toDomain(UserRequest) ─────────────────────────────────────

    @Test
    void givenNullUserRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((UserStudentRequest) null));
    }

    @Test
    void givenValidUserRequest_whenToDomain_thenMapsAllFields() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Juan Diaz");
        request.setEmail("juan@escuelaing.edu.co");
        request.setPassword("TestPassword123");
        request.setGender(GenderEnum.MALE);
        request.setCareer(CareerEnum.COMPUTER_SCIENCE);
        request.setSemester(6);
        request.setStudentCarnet("2021123431");
        request.setPhotourl("http://foto.jpg");
        request.setBiography("Bio de Juan");
        request.setPrivacyLevel(PrivacyLevelEnum.PRIVATE);
        request.setDateOfBirth(LocalDate.of(1999, 10, 5));

        TagRequest tagRequest = new TagRequest();
        tagRequest.setTagId(UUID.randomUUID());

        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.setDayOfWeek(DayOfWeekEnum.THURSDAY);
        scheduleRequest.setName("Base de Datos");
        scheduleRequest.setStartTime(LocalTime.of(7, 0));
        scheduleRequest.setEndTime(LocalTime.of(9, 0));

        StudentProfile result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Juan Diaz", result.getName());
        assertEquals("juan@escuelaing.edu.co", result.getEmail());
        assertEquals(GenderEnum.MALE, result.getGender());
        assertEquals(CareerEnum.COMPUTER_SCIENCE, result.getCareer());
        assertEquals(6, result.getSemester());
        assertEquals("http://foto.jpg", result.getPhotoUrl());
        assertEquals("Bio de Juan", result.getBiography());
        assertEquals(PrivacyLevelEnum.PRIVATE, result.getPrivacyLevel());
        assertEquals(LocalDate.of(1999, 10, 5), result.getDateOfBirth());

        assertTrue(result.getTagsId().isEmpty());

        assertTrue(result.getSchedulesAvailability().isEmpty());
    }

    @Test
    void givenUserRequestWithNullSemester_whenToDomain_thenSemesterIsZero() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Test User");
        request.setEmail("test@escuelaing.edu.co");
        request.setPassword("TestPassword123");
        request.setGender(GenderEnum.MALE);
        request.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        request.setPhotourl("http://photo.jpg");
        request.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        request.setSemester(null);

        StudentProfile result = mapper.toDomain(request);

        assertNull(result.getSemester());
    }

    @Test
    void givenUserRequestWithNullTags_whenToDomain_thenTagsAreNull() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Test User");
        request.setEmail("test@escuelaing.edu.co");
        request.setPassword("TestPassword123");
        request.setGender(GenderEnum.MALE);
        request.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        request.setPhotourl("http://photo.jpg");
        request.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);

        StudentProfile result = mapper.toDomain(request);

        assertTrue(result.getTagsId().isEmpty());
    }

    @Test
    void givenUserRequestWithNullSchedules_whenToDomain_thenSchedulesAreNull() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Test User");
        request.setEmail("test@escuelaing.edu.co");
        request.setPassword("TestPassword123");
        request.setGender(GenderEnum.MALE);
        request.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        request.setPhotourl("http://photo.jpg");
        request.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);

        StudentProfile result = mapper.toDomain(request);

        assertTrue(result.getSchedulesAvailability().isEmpty());
    }

    // ── toDomain(ScheduleRequest) ─────────────────────────────────

    @Test
    void givenNullScheduleRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((ScheduleRequest) null));
    }

    @Test
    void givenValidScheduleRequest_whenToDomain_thenMapsAllFields() {
        ScheduleRequest request = new ScheduleRequest();
        request.setDayOfWeek(DayOfWeekEnum.FRIDAY);
        request.setName("Seminario de Grado");
        request.setStartTime(LocalTime.of(15, 0));
        request.setEndTime(LocalTime.of(17, 0));

        Schedule result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals(DayOfWeekEnum.FRIDAY, result.getDayOfWeek());
        assertEquals("Seminario de Grado", result.getName());
        assertEquals(LocalTime.of(15, 0), result.getStartTime());
        assertEquals(LocalTime.of(17, 0), result.getEndTime());
    }
    
    // ── toDomain(UserAdminRequest) ────────────────────────────────

    @Test
    void givenNullAdminRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((UserAdminRequest) null));
    }

    @Test
    void givenValidAdminRequest_whenToDomain_thenMapsAllFields() {
        UserAdminRequest request = new UserAdminRequest();
        request.setName("Admin User");
        request.setEmail("admin@escuelaing.edu.co");
        request.setPassword("AdminPass123");
        request.setGender(GenderEnum.MALE);

        Admin result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Admin User", result.getName());
        assertEquals("admin@escuelaing.edu.co", result.getEmail());
        assertEquals("AdminPass123", result.getPasswordHash());
        assertEquals(GenderEnum.MALE, result.getGender());
    }

    // ── toDomain(UserAdminUpdateRequest) ─────────────────────────

    @Test
    void givenNullAdminUpdateRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((UserAdminUpdateRequest) null));
    }

    @Test
    void givenValidAdminUpdateRequest_whenToDomain_thenMapsAllFields() {
        UserAdminUpdateRequest request = new UserAdminUpdateRequest();
        request.setName("Updated Admin");
        request.setEmail("updated@escuelaing.edu.co");
        request.setGender(GenderEnum.FEMALE);

        Admin result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Updated Admin", result.getName());
        assertEquals("updated@escuelaing.edu.co", result.getEmail());
        assertEquals(GenderEnum.FEMALE, result.getGender());
    }

    // ── toDomain(UserOrganizerRequest) ───────────────────────────

    @Test
    void givenNullOrganizerRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((UserOrganizerRequest) null));
    }

    @Test
    void givenValidOrganizerRequest_whenToDomain_thenMapsAllFields() {
        UserOrganizerRequest request = new UserOrganizerRequest();
        request.setName("Event Org");
        request.setEmail("org@escuelaing.edu.co");
        request.setPassword("OrgPass123");
        request.setGender(GenderEnum.FEMALE);
        request.setContactInfo("contact@evento.co");

        Organizer result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Event Org", result.getName());
        assertEquals("org@escuelaing.edu.co", result.getEmail());
        assertEquals("OrgPass123", result.getPasswordHash());
        assertEquals("contact@evento.co", result.getContactInfo());
    }

    // ── toDomain(UserOrganizerUpdateRequest) ─────────────────────

    @Test
    void givenNullOrganizerUpdateRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((UserOrganizerUpdateRequest) null));
    }

    @Test
    void givenValidOrganizerUpdateRequest_whenToDomain_thenMapsAllFields() {
        UserOrganizerUpdateRequest request = new UserOrganizerUpdateRequest();
        request.setName("Updated Org");
        request.setEmail("updated-org@escuelaing.edu.co");
        request.setGender(GenderEnum.MALE);
        request.setContactInfo("new@evento.co");

        Organizer result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Updated Org", result.getName());
        assertEquals("new@evento.co", result.getContactInfo());
    }

    // ── toDomain(UserStudentUpdateRequest) ───────────────────────

    @Test
    void givenNullStudentUpdateRequest_whenToDomain_thenReturnsNull() {
        assertNull(mapper.toDomain((UserStudentUpdateRequest) null));
    }

    @Test
    void givenValidStudentUpdateRequest_whenToDomain_thenMapsAllFields() {
        UserStudentUpdateRequest request = new UserStudentUpdateRequest();
        request.setName("Updated Student");
        request.setEmail("updated@escuelaing.edu.co");
        request.setGender(GenderEnum.MALE);
        request.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        request.setSemester(5);
        request.setStudentCarnet("2021123411");
        request.setBiography("Nueva bio");
        request.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));

        StudentProfile result = mapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Updated Student", result.getName());
        assertEquals(CareerEnum.SYSTEMS_ENGINEERING, result.getCareer());
        assertEquals(5, result.getSemester());
        assertEquals("Nueva bio", result.getBiography());
    }

    @Test
    void givenStudentUpdateRequestWithNullSemester_whenToDomain_thenSemesterIsZero() {
        UserStudentUpdateRequest request = new UserStudentUpdateRequest();
        request.setName("Test User");
        request.setEmail("test@escuelaing.edu.co");
        request.setGender(GenderEnum.MALE);
        request.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        request.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        request.setSemester(null);

        StudentProfile result = mapper.toDomain(request);

        assertNull(result.getSemester());
    }
}
