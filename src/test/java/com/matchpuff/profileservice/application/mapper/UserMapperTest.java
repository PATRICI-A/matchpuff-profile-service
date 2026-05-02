package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.TagResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        // Implementación anónima para testear los métodos default de la interfaz
        mapper = new UserMapper() {
            @Override
            public UserResponse toResponse(User user) {
                return null;
            }
        };
    }

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
        assertNull(mapper.toResponseInfo(null));
    }

    @Test
    void givenStudentProfile_whenToResponseInfo_thenMapsAllFields() {
        StudentProfile student = new StudentProfile();
        student.setId("s-1");
        student.setName("Ana Ruiz");
        student.setEmail("ana@escuelaing.edu.co");
        student.setGender(GenderEnum.FEMALE);
        student.setDateOfBirth(LocalDate.of(1999, 3, 10));
        student.setBiography("Estudiante apasionada");
        student.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));

        Tag tag = new Tag();
        tag.setName("Spring Boot");
        tag.setCategory("Backend");
        student.setTags(List.of(tag));

        Schedule schedule = new Schedule();
        schedule.setName("Algoritmos");
        schedule.setDayOfWeek(DayOfWeekEnum.TUESDAY);
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(11, 0));
        student.setSchedules(List.of(schedule));

        UserResponseInfo info = mapper.toResponseInfo(student);

        assertEquals("s-1", info.getId());
        assertEquals("Ana Ruiz", info.getName());
        assertEquals("ana@escuelaing.edu.co", info.getEmail());
        assertEquals("STUDENT", info.getUserType());
        assertEquals("FEMALE", info.getGender());
        assertEquals("Estudiante apasionada", info.getBiography());
        assertNotNull(info.getTags());
        assertEquals(1, info.getTags().size());
        assertNotNull(info.getSchedules());
        assertEquals(1, info.getSchedules().size());
    }

    @Test
    void givenStudentWithNullSchedulesAndTags_whenToResponseInfo_thenSchedulesAndTagsAreNull() {
        StudentProfile student = new StudentProfile();
        student.setId("s-2");
        student.setName("Pedro Gomez");
        student.setEmail("pedro@escuelaing.edu.co");
        student.setSchedules(null);
        student.setTags(null);

        UserResponseInfo info = mapper.toResponseInfo(student);

        assertNull(info.getSchedules());
        assertNull(info.getTags());
    }

    @Test
    void givenAdmin_whenToResponseInfo_thenBiographySchedulesAndTagsAreNull() {
        Admin admin = new Admin();
        admin.setId("a-1");
        admin.setName("Admin User");
        admin.setEmail("admin@escuelaing.edu.co");

        UserResponseInfo info = mapper.toResponseInfo(admin);

        assertEquals("ADMIN", info.getUserType());
        assertEquals("Admin User", info.getName());
        assertNull(info.getBiography());
        assertNull(info.getSchedules());
        assertNull(info.getTags());
    }

    @Test
    void givenOrganizer_whenToResponseInfo_thenUserTypeIsORGANIZER() {
        Organizer organizer = new Organizer();
        organizer.setId("o-1");
        organizer.setName("Evento Corp");
        organizer.setEmail("org@escuelaing.edu.co");

        UserResponseInfo info = mapper.toResponseInfo(organizer);

        assertEquals("ORGANIZER", info.getUserType());
        assertEquals("o-1", info.getId());
    }

    @Test
    void givenStudentWithGenderAndDob_whenToResponseInfo_thenGenderAndDobAreMapped() {
        StudentProfile student = new StudentProfile();
        student.setId("s-3");
        student.setName("Luis");
        student.setEmail("luis@escuelaing.edu.co");
        student.setGender(GenderEnum.MALE);
        student.setDateOfBirth(LocalDate.of(2001, 6, 20));

        UserResponseInfo info = mapper.toResponseInfo(student);

        assertEquals("MALE", info.getGender());
        assertEquals("2001-06-20", info.getDateOfBirth());
    }

    // ── toScheduleResponseList ────────────────────────────────────

    @Test
    void givenNullList_whenToScheduleResponseList_thenReturnsNull() {
        assertNull(mapper.toScheduleResponseList(null));
    }

    @Test
    void givenEmptyList_whenToScheduleResponseList_thenReturnsEmpty() {
        List<ScheduleResponse> result = mapper.toScheduleResponseList(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void givenScheduleList_whenToScheduleResponseList_thenMapsAllFields() {
        Schedule s = new Schedule();
        s.setDayOfWeek(DayOfWeekEnum.FRIDAY);
        s.setName("Seminario");
        s.setStartTime(LocalTime.of(14, 0));
        s.setEndTime(LocalTime.of(16, 0));

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
        assertNull(mapper.toTagResponseList(null));
    }

    @Test
    void givenEmptyList_whenToTagResponseList_thenReturnsEmpty() {
        List<TagResponse> result = mapper.toTagResponseList(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void givenTagList_whenToTagResponseList_thenMapsAllFields() {
        Tag tag = new Tag();
        tag.setName("Docker");
        tag.setCategory("DevOps");

        List<TagResponse> result = mapper.toTagResponseList(List.of(tag));

        assertEquals(1, result.size());
        assertEquals("Docker", result.get(0).getName());
        assertEquals("DevOps", result.get(0).getCategory());
    }

    @Test
    void givenMultipleTags_whenToTagResponseList_thenMapsAll() {
        Tag t1 = new Tag();
        t1.setName("Java");
        t1.setCategory("Backend");

        Tag t2 = new Tag();
        t2.setName("React");
        t2.setCategory("Frontend");

        List<TagResponse> result = mapper.toTagResponseList(List.of(t1, t2));

        assertEquals(2, result.size());
    }
}
