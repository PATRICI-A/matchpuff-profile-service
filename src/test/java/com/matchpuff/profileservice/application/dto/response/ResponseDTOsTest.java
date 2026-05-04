package com.matchpuff.profileservice.application.dto.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.matchpuff.profileservice.domain.model.enums.CareerEnum;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Response DTOs Tests")
class ResponseDTOsTest {

    @Test
    void givenAdminResponse_whenBuilder_thenFieldsAreSet() {
        // Given
        String id = "admin-123";
        String name = "Admin User";
        String email = "admin@example.com";
        LocalDateTime createdAt = LocalDateTime.now();
        String gender = "MALE";
        String userType = "ADMIN";

        // When
        AdminResponse adminResponse = AdminResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .createdAt(createdAt)
                .gender(gender)
                .userType(userType)
                .build();

        // Then
        assertEquals(id, adminResponse.getId());
        assertEquals(name, adminResponse.getName());
        assertEquals(email, adminResponse.getEmail());
        assertEquals(createdAt, adminResponse.getCreatedAt());
        assertEquals(gender, adminResponse.getGender());
        assertEquals(userType, adminResponse.getUserType());
    }

    @Test
    void givenOrganizerResponse_whenBuilder_thenAllFieldsAreSet() {
        // Given
        String id = "org-123";
        String name = "Organizer User";
        String email = "organizer@example.com";
        LocalDateTime createdAt = LocalDateTime.now();
        String gender = "FEMALE";
        String userType = "ORGANIZER";
        String contactInfo = "3001234567";

        // When
        OrganizerResponse organizerResponse = OrganizerResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .createdAt(createdAt)
                .gender(gender)
                .userType(userType)
                .contactInfo(contactInfo)
                .build();

        // Then
        assertEquals(id, organizerResponse.getId());
        assertEquals(name, organizerResponse.getName());
        assertEquals(email, organizerResponse.getEmail());
        assertEquals(createdAt, organizerResponse.getCreatedAt());
        assertEquals(gender, organizerResponse.getGender());
        assertEquals(userType, organizerResponse.getUserType());
        assertEquals(contactInfo, organizerResponse.getContactInfo());
    }

    @Test
    void givenStudentProfileResponse_whenBuilder_thenAllFieldsAreSet() {
        // Given
        String id = "student-123";
        String name = "Student User";
        String email = "student@escuelaing.edu.co";
        LocalDateTime createdAt = LocalDateTime.now();
        String gender = "MALE";
        String userType = "STUDENT";
        String dateOfBirth = "2001-05-15";
        CareerEnum career = CareerEnum.SYSTEMS_ENGINEERING;
        int semester = 5;
        String photoUrl = "http://example.com/photo.jpg";
        String biography = "Student bio";
        PrivacyLevelEnum privacyLevel = PrivacyLevelEnum.PRIVATE;
        List<ScheduleResponse> schedules = new ArrayList<>();
        List<TagResponse> tags = new ArrayList<>();

        // When
        StudentProfileResponse studentResponse = StudentProfileResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .createdAt(createdAt)
                .gender(gender)
                .userType(userType)
                .dateOfBirth(dateOfBirth)
                .career(career)
                .semester(semester)
                .photoUrl(photoUrl)
                .biography(biography)
                .privacyLevel(privacyLevel)
                .schedules(schedules)
                .tags(tags)
                .build();

        // Then
        assertEquals(id, studentResponse.getId());
        assertEquals(name, studentResponse.getName());
        assertEquals(email, studentResponse.getEmail());
        assertEquals(createdAt, studentResponse.getCreatedAt());
        assertEquals(gender, studentResponse.getGender());
        assertEquals(userType, studentResponse.getUserType());
        assertEquals(dateOfBirth, studentResponse.getDateOfBirth());
        assertEquals(career, studentResponse.getCareer());
        assertEquals(semester, studentResponse.getSemester());
        assertEquals(photoUrl, studentResponse.getPhotoUrl());
        assertEquals(biography, studentResponse.getBiography());
        assertEquals(privacyLevel, studentResponse.getPrivacyLevel());
        assertEquals(schedules, studentResponse.getSchedules());
        assertEquals(tags, studentResponse.getTags());
    }

    @Test
    void givenUserResponse_whenBuilder_thenBaseFieldsAreSet() {
        // Given
        String id = "user-123";
        String name = "Regular User";
        String email = "user@example.com";
        LocalDateTime createdAt = LocalDateTime.now();
        String gender = "OTHER";
        String userType = "USER";

        // When
        UserResponse userResponse = UserResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .createdAt(createdAt)
                .gender(gender)
                .userType(userType)
                .build();

        // Then
        assertEquals(id, userResponse.getId());
        assertEquals(name, userResponse.getName());
        assertEquals(email, userResponse.getEmail());
        assertEquals(createdAt, userResponse.getCreatedAt());
        assertEquals(gender, userResponse.getGender());
        assertEquals(userType, userResponse.getUserType());
    }

    @Test
    void givenAdminResponse_whenSetters_thenFieldsAreUpdated() {
        // Given
        AdminResponse adminResponse = new AdminResponse();

        // When
        adminResponse.setId("updated-id");
        adminResponse.setName("Updated Admin");
        adminResponse.setEmail("updated@example.com");

        // Then
        assertEquals("updated-id", adminResponse.getId());
        assertEquals("Updated Admin", adminResponse.getName());
        assertEquals("updated@example.com", adminResponse.getEmail());
    }

    @Test
    void givenOrganizerResponse_whenSetters_thenAllFieldsUpdated() {
        // Given
        OrganizerResponse organizerResponse = new OrganizerResponse();

        // When
        organizerResponse.setContactInfo("3009876543");

        // Then
        assertEquals("3009876543", organizerResponse.getContactInfo());
    }

    @Test
    void givenStudentProfileResponse_whenSetters_thenSpecificFieldsUpdated() {
        // Given
        StudentProfileResponse studentResponse = new StudentProfileResponse();
        List<TagResponse> newTags = new ArrayList<>();

        // When
        studentResponse.setPhotoUrl("http://new-photo.jpg");
        studentResponse.setBiography("Updated bio");
        studentResponse.setTags(newTags);

        // Then
        assertEquals("http://new-photo.jpg", studentResponse.getPhotoUrl());
        assertEquals("Updated bio", studentResponse.getBiography());
        assertEquals(newTags, studentResponse.getTags());
    }
}
