package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.response.*;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper = new UserMapperImpl();

    // ── resolveUserType ───────────────────────────────────────────

    @Test
    void givenStudentProfile_whenResolveUserType_thenReturnsSTUDENT() {
        assertEquals("STUDENT", mapper.resolveUserType(new StudentProfile()));
    }

    @Test
    void givenAdmin_whenResolveUserType_thenReturnsADMIN() {
        assertEquals("ADMIN", mapper.resolveUserType(new Admin()));
    }

    @Test
    void givenOrganizer_whenResolveUserType_thenReturnsORGANIZER() {
        assertEquals("ORGANIZER", mapper.resolveUserType(new Organizer()));
    }

    @Test
    void givenGenericUser_whenResolveUserType_thenReturnsNull() {
        User generic = new User() {};
        assertNull(mapper.resolveUserType(generic));
    }

    // ── toResponseInfo ────────────────────────────────────────────

    @Test
    void givenNull_whenToResponseInfo_thenReturnsNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void givenStudentProfile_whenToResponseInfo_thenMapsAllFields() {
        StudentProfile student = new StudentProfile();
        student.setId(UUID.randomUUID());
        student.setName("Ana Ruiz");
        student.setEmail("ana@escuelaing.edu.co");
        student.setGender(GenderEnum.FEMALE);
        student.setDateOfBirth(LocalDate.of(1999, 3, 10));
        student.setStudentCarnet("2021123412");
        student.setBiography("Estudiante apasionada");
        student.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));

        Tag tag = new Tag("Spring Boot", "Backend");
        student.setTags(List.of(tag));

        Schedule schedule = new Schedule( DayOfWeekEnum.TUESDAY, "Algoritmos", LocalTime.of(9, 0), LocalTime.of(11, 0));
        student.setSchedules(List.of(schedule));

        UserResponse info = mapper.toResponse(student);
        assertTrue(info instanceof StudentProfileResponse);
        StudentProfileResponse studentInfo = (StudentProfileResponse) info;

        assertEquals(student.getId(), studentInfo.getId());
        assertEquals("Ana Ruiz", studentInfo.getName());
        assertEquals("ana@escuelaing.edu.co", studentInfo.getEmail());
        assertEquals("STUDENT", studentInfo.getUserType());
        assertEquals("FEMALE", studentInfo.getGender());
        assertEquals("2021123412", studentInfo.getStudentCarnet());
        assertEquals("Estudiante apasionada", studentInfo.getBiography());
        assertNotNull(studentInfo.getTags());
        assertEquals(1, studentInfo.getTags().size());
        assertNotNull(studentInfo.getSchedules());
        assertEquals(1, studentInfo.getSchedules().size());
    }

    @Test
    void givenStudentWithNullSchedulesAndTags_whenToResponseInfo_thenSchedulesAndTagsAreNull() {
        StudentProfile student = new StudentProfile();
        student.setId(UUID.randomUUID());
        student.setName("Pedro Gomez");
        student.setEmail("pedro@escuelaing.edu.co");
        student.setSchedules(null);
        student.setTags(null);

        UserResponse info = mapper.toResponse(student);
        assertTrue(info instanceof StudentProfileResponse);
        StudentProfileResponse studentInfo = (StudentProfileResponse) info;

        assertTrue(studentInfo.getSchedules().isEmpty());
        assertTrue(studentInfo.getTags().isEmpty());
    }

    @Test
    void givenAdmin_whenToResponseInfo_thenBiographySchedulesAndTagsAreNull() {
        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setName("Admin User");
        admin.setEmail("admin@escuelaing.edu.co");

        UserResponse info = mapper.toResponse(admin);
        assertTrue(info instanceof AdminResponse);
        AdminResponse adminInfo = (AdminResponse) info;

        assertEquals("ADMIN", adminInfo.getUserType());
        assertEquals("Admin User", adminInfo.getName());
    }

    @Test
    void givenOrganizer_whenToResponseInfo_thenUserTypeIsORGANIZER() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());
        organizer.setName("Evento Corp");
        organizer.setEmail("org@escuelaing.edu.co");
        organizer.setContactInfo("contact@example.com");

        UserResponse info = mapper.toResponse(organizer);
        assertTrue(info instanceof OrganizerResponse);
        OrganizerResponse orgInfo = (OrganizerResponse) info;

        assertEquals("ORGANIZER", orgInfo.getUserType());
        assertEquals(organizer.getId(), orgInfo.getId());
        assertEquals("contact@example.com", orgInfo.getContactInfo());
    }

    @Test
    void givenStudentWithGenderAndDob_whenToResponseInfo_thenGenderAndDobAreMapped() {
        StudentProfile student = new StudentProfile();
        student.setId(UUID.randomUUID());
        student.setName("Luis");
        student.setEmail("luis@escuelaing.edu.co");
        student.setGender(GenderEnum.MALE);
        student.setDateOfBirth(LocalDate.of(2001, 6, 20));

        UserResponse info = mapper.toResponse(student);
        assertTrue(info instanceof StudentProfileResponse);
        StudentProfileResponse studentInfo = (StudentProfileResponse) info;

        assertEquals("MALE", studentInfo.getGender());
    }

    // ── toScheduleResponseList ────────────────────────────────────

    @Test
    void givenNullList_whenToScheduleResponseList_thenReturnsNull() {
        assertTrue(mapper.toScheduleResponseList(null).isEmpty());
    }

    @Test
    void givenEmptyList_whenToScheduleResponseList_thenReturnsEmpty() {
        List<ScheduleResponse> result = mapper.toScheduleResponseList(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void givenScheduleList_whenToScheduleResponseList_thenMapsAllFields() {
        Schedule s = new Schedule( DayOfWeekEnum.FRIDAY, "Seminario", LocalTime.of(14, 0), LocalTime.of(16, 0));
        List<ScheduleResponse> result = mapper.toScheduleResponseList(List.of(s));

        assertEquals(1, result.size());
        assertEquals("Seminario", result.get(0).getName());
        assertEquals(DayOfWeekEnum.FRIDAY, result.get(0).getDayOfWeek());
        assertEquals(LocalTime.of(14, 0), result.get(0).getStartTime());
        assertEquals(LocalTime.of(16, 0), result.get(0).getEndTime());
    }

    // ── toTagResponseList ─────────────────────────────────────────

    @Test
    void givenNullList_whenToTagResponseList_thenReturnsNull() {
        assertTrue(mapper.toTagResponseList(null).isEmpty());
    }

    @Test
    void givenEmptyList_whenToTagResponseList_thenReturnsEmpty() {
        List<TagResponse> result = mapper.toTagResponseList(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void givenTagList_whenToTagResponseList_thenMapsAllFields() {
        Tag tag = new Tag("Docker", "DevOps");
        List<TagResponse> result = mapper.toTagResponseList(List.of(tag));

        assertEquals(1, result.size());
        assertEquals("Docker", result.get(0).getName());
        assertEquals("DevOps", result.get(0).getCategory());
    }

    @Test
    void givenMultipleTags_whenToTagResponseList_thenMapsAll() {
        Tag t1 = new Tag("Java", "Backend");
        t1.setCategory("Backend");

        Tag t2 = new Tag("React", "Frontend");
        t2.setCategory("Frontend");

        List<TagResponse> result = mapper.toTagResponseList(List.of(t1, t2));

        assertEquals(2, result.size());
    }

    // ── toResponseProfilePhoto ───────────────────────────────────

    @Test
    void givenStudentWithPhoto_whenToResponseProfilePhoto_thenReturnsPhotoInfo() {
        StudentProfile s = new StudentProfile();
        s.setId(UUID.randomUUID());
        s.setName("Photo User");
        s.setEmail("photo@escuelaing.edu.co");
        s.setPhotoUrl("http://photo.png");

        UserResponseProfilePhoto p = mapper.toResponseProfilePhoto(s);
        assertNotNull(p);
        assertEquals("http://photo.png", p.getProfileImageUrl());
    }

    @Test
    void givenNonStudent_whenToResponseProfilePhoto_thenReturnsNull() {
        Admin a = new Admin();
        a.setId(UUID.randomUUID());
        assertNull(mapper.toResponseProfilePhoto(a));
    }
}
