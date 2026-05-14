package com.matchpuff.profileservice.domain.model;

import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentProfileTest {

    private StudentProfile student;

    @BeforeEach
    void setUp() {
        student = new StudentProfile();
        student.setName("Maria Lopez");
        student.setEmail("maria@escuelaing.edu.co");
        student.setGender(GenderEnum.FEMALE);
        student.setDateOfBirth(LocalDate.of(2000, 5, 15));
    }

    @Test
    void givenValidBiography_whenSet_thenGetBiographyReturnsIt() {
        student.setBiography("Estudiante de sistemas");
        assertEquals("Estudiante de sistemas", student.getBiography());
    }

    @Test
    void givenNullBiography_whenSet_thenGetBiographyReturnsNull() {
        student.setBiography(null);
        assertNull(student.getBiography());
    }

    @Test
    void givenBiographyOver200Chars_whenSet_thenThrowsInvalidInputException() {
        String longBio = "x".repeat(201);
        assertThrows(InvalidInputException.class, () -> student.setBiography(longBio));
    }

    @Test
    void givenCareer_whenSet_thenGetCareerReturnsIt() {
        student.setCareer(CareerEnum.SYSTEMS_ENGINEERING);
        assertEquals(CareerEnum.SYSTEMS_ENGINEERING, student.getCareer());
    }

    @Test
    void givenSemester_whenSet_thenGetSemesterReturnsIt() {
        student.setSemester(5);
        assertEquals(5, student.getSemester());
    }

    @Test
    void givenPhotoUrl_whenSet_thenGetPhotoUrlReturnsIt() {
        student.setPhotoUrl("http://example.com/photo.jpg");
        assertEquals("http://example.com/photo.jpg", student.getPhotoUrl());
    }

    @Test
    void givenPrivacyLevel_whenSet_thenGetPrivacyLevelReturnsIt() {
        student.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);
        assertEquals(PrivacyLevelEnum.PUBLIC, student.getPrivacyLevel());
    }

    @Test
    void givenSchedules_whenSet_thenGetSchedulesReturnsThem() {
        Schedule schedule = new Schedule( DayOfWeekEnum.MONDAY, "Algoritmos", LocalTime.of(8, 0), LocalTime.of(10, 0));
        student.setSchedulesAvailability(List.of(schedule));
        assertEquals(1, student.getSchedulesAvailability().size());
    }

    @Test
    void givenInheritedEmailField_whenSet_thenGetEmailReturnsString() {
        assertEquals("maria@escuelaing.edu.co", student.getEmail());
    }

    @Test
    void givenInvalidName_whenSet_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> student.setName("A"));
    }

    @Test
    void givenFutureDateOfBirth_whenSet_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> student.setDateOfBirth(LocalDate.now().plusDays(1)));
    }

    @Test
    void givenNullCareer_whenSet_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> student.setCareer(null));
    }

    @Test
    void givenInvalidSemester_whenSet_thenThrowsInvalidInputException() {
        assertDoesNotThrow(() -> student.setSemester(null));
        assertThrows(InvalidInputException.class, () -> student.setSemester(0));
        assertThrows(InvalidInputException.class, () -> student.setSemester(11));
    }

    @Test
    void givenBlankPhotoUrl_whenSet_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> student.setPhotoUrl("   "));
    }

    @Test
    void givenNullPrivacyLevel_whenSet_thenThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class,
                () -> student.setPrivacyLevel(null));
    }
}
