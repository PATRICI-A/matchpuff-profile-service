package com.matchpuff.profileservice.application.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.GenderEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DisplayName("UserStudentRequest Tests")
class UserStudentRequestTest {

    private UserStudentRequest userStudentRequest;
    private List<TagRequest> tags;
    private List<ScheduleRequest> schedules;

    @BeforeEach
    void setUp() {
        userStudentRequest = new UserStudentRequest();
        tags = new ArrayList<>();
        tags.add(createTagRequest("Programming"));
        schedules = new ArrayList<>();
        schedules.add(createScheduleRequest());
    }

    @Test
    void givenValidUserStudentRequest_whenCreated_thenAllFieldsSet() {
        // Given & When
        userStudentRequest.setName("Juan Perez");
        userStudentRequest.setEmail("juan.perez@escuelaing.edu.co");
        userStudentRequest.setGender(GenderEnum.MALE);
        userStudentRequest.setCarreer(CareerEnum.SYSTEMS_ENGINEERING);
        userStudentRequest.setSemester(5);
        userStudentRequest.setPhotourl("http://example.com/photo.jpg");
        userStudentRequest.setBiography("Student biography");
        userStudentRequest.setPrivacyLevel(PrivacyLevelEnum.PRIVATE);
        userStudentRequest.setDateOfBirth(LocalDate.of(2001, 5, 15));

        // Then
        assertEquals("Juan Perez", userStudentRequest.getName());
        assertEquals("juan.perez@escuelaing.edu.co", userStudentRequest.getEmail());
        assertEquals(GenderEnum.MALE, userStudentRequest.getGender());
        assertEquals(CareerEnum.SYSTEMS_ENGINEERING, userStudentRequest.getCarreer());
        assertEquals(5, userStudentRequest.getSemester());
        assertEquals("http://example.com/photo.jpg", userStudentRequest.getPhotourl());
        assertEquals("Student biography", userStudentRequest.getBiography());
        assertEquals(PrivacyLevelEnum.PRIVATE, userStudentRequest.getPrivacyLevel());
    }

    @Test
    void givenUserStudentRequest_whenSetName_thenNameIsUpdated() {
        // When
        userStudentRequest.setName("Maria Garcia");

        // Then
        assertEquals("Maria Garcia", userStudentRequest.getName());
    }

    @Test
    void givenUserStudentRequest_whenSetSemester_thenSemesterIsUpdated() {
        // When
        userStudentRequest.setSemester(8);

        // Then
        assertEquals(8, userStudentRequest.getSemester());
    }

    @Test
    void givenUserStudentRequest_whenSetGender_thenGenderIsUpdated() {
        // When
        userStudentRequest.setGender(GenderEnum.FEMALE);

        // Then
        assertEquals(GenderEnum.FEMALE, userStudentRequest.getGender());
    }

    @Test
    void givenUserStudentRequest_whenSetCareer_thenCareerIsUpdated() {
        // When
        userStudentRequest.setCarreer(CareerEnum.SYSTEMS_ENGINEERING);

        // Then
        assertEquals(CareerEnum.SYSTEMS_ENGINEERING, userStudentRequest.getCarreer());
    }

    @Test
    void givenUserStudentRequest_whenSetPrivacyLevel_thenPrivacyLevelIsUpdated() {
        // When
        userStudentRequest.setPrivacyLevel(PrivacyLevelEnum.PUBLIC);

        // Then
        assertEquals(PrivacyLevelEnum.PUBLIC, userStudentRequest.getPrivacyLevel());
    }

    @Test
    void givenUserStudentRequest_whenSetBiography_thenBiographyIsUpdated() {
        // When
        String biography = "I love programming and solving complex problems";
        userStudentRequest.setBiography(biography);

        // Then
        assertEquals(biography, userStudentRequest.getBiography());
    }

    private TagRequest createTagRequest(String name) {
        TagRequest tagRequest = new TagRequest();
        tagRequest.setName(name);
        return tagRequest;
    }

    private ScheduleRequest createScheduleRequest() {
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.setName("Algorithm Class");
        return scheduleRequest;
    }
}
