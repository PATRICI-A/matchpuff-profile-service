package com.matchpuff.profileservice.entrypoints.rest.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class UserRestMapperTest {

    // ── toDomain(UserRequest) ─────────────────────────────────────

    @Test
    void givenNullUserRequest_whenToDomain_thenReturnsNull() {
        assertNull(UserRestMapper.toDomain((UserStudentRequest) null));
    }

    @Test
    void givenValidUserRequest_whenToDomain_thenMapsAllFields() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Juan Diaz");
        request.setEmail("juan@escuelaing.edu.co");
        request.setPassword("TestPassword123");
        request.setGender(GenderEnum.MALE);
        request.setCarreer(CareerEnum.COMPUTER_SCIENCE);
        request.setSemester(6);
        request.setStudentCarnet(Long.valueOf(20211234));
        request.setPhotourl("http://foto.jpg");
        request.setBiography("Bio de Juan");
        request.setPrivacyLevel(PrivacyLevelEnum.PRIVATE);
        request.setDateOfBirth(LocalDate.of(1999, 10, 5));

        TagRequest tagRequest = new TagRequest();
        tagRequest.setName("Python");
        tagRequest.setCategory("IA");

        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.setDayOfWeek(DayOfWeekEnum.THURSDAY);
        scheduleRequest.setName("Base de Datos");
        scheduleRequest.setStartTime(LocalTime.of(7, 0));
        scheduleRequest.setEndTime(LocalTime.of(9, 0));

        StudentProfile result = UserRestMapper.toDomain(request);

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

        assertTrue(result.getTags().isEmpty());

        assertTrue(result.getSchedules().isEmpty());
    }

    @Test
    void givenUserRequestWithNullSemester_whenToDomain_thenSemesterIsZero() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Test");
        request.setEmail("test@escuelaing.edu.co");
        request.setPassword("TestPassword123");
        request.setSemester(null);

        StudentProfile result = UserRestMapper.toDomain(request);

        assertEquals(0, result.getSemester());
    }

    @Test
    void givenUserRequestWithNullTags_whenToDomain_thenTagsAreNull() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Test");
        request.setEmail("test@escuelaing.edu.co");
        request.setPassword("TestPassword123");

        StudentProfile result = UserRestMapper.toDomain(request);

        assertTrue(result.getTags().isEmpty());
    }

    @Test
    void givenUserRequestWithNullSchedules_whenToDomain_thenSchedulesAreNull() {
        UserStudentRequest request = new UserStudentRequest();
        request.setName("Test");
        request.setEmail("test@escuelaing.edu.co");
        request.setPassword("TestPassword123");

        StudentProfile result = UserRestMapper.toDomain(request);

        assertTrue(result.getSchedules().isEmpty());
    }

    // ── toDomain(ScheduleRequest) ─────────────────────────────────

    @Test
    void givenNullScheduleRequest_whenToDomain_thenReturnsNull() {
        assertNull(UserRestMapper.toDomain((ScheduleRequest) null));
    }

    @Test
    void givenValidScheduleRequest_whenToDomain_thenMapsAllFields() {
        ScheduleRequest request = new ScheduleRequest();
        request.setDayOfWeek(DayOfWeekEnum.FRIDAY);
        request.setName("Seminario de Grado");
        request.setStartTime(LocalTime.of(15, 0));
        request.setEndTime(LocalTime.of(17, 0));

        Schedule result = UserRestMapper.toDomain(request);

        assertNotNull(result);
        assertEquals(DayOfWeekEnum.FRIDAY, result.getDayOfWeek());
        assertEquals("Seminario de Grado", result.getName());
        assertEquals(LocalTime.of(15, 0), result.getStartTime());
        assertEquals(LocalTime.of(17, 0), result.getEndTime());
    }

    // ── toDomain(TagRequest) ──────────────────────────────────────

    @Test
    void givenNullTagRequest_whenToDomain_thenReturnsNull() {
        assertNull(UserRestMapper.toDomain((TagRequest) null));
    }

    @Test
    void givenValidTagRequest_whenToDomain_thenMapsAllFields() {
        TagRequest request = new TagRequest();
        request.setName("Kubernetes");
        request.setCategory("DevOps");

        Tag result = UserRestMapper.toDomain(request);

        assertNotNull(result);
        assertEquals("Kubernetes", result.getName());
        assertEquals("DevOps", result.getCategory());
    }
}
