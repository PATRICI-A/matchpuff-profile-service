package com.matchpuff.profileservice.application.service;

import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternalUserServiceTest {

    @Mock
    private UserUseCasePort userUseCase;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private InternalUserService internalUserService;

    // ── getUser ───────────────────────────────────────────────────

    @Test
    void givenStudentId_whenGetUser_thenReturnsMappedAuthResponse() {
        UUID userId = UUID.randomUUID();
        StudentProfile student = new StudentProfile();
        student.setId(userId);

        UserAuthResponse response = UserAuthResponse.builder()
                .id(student.getId())
                .email("carlos@escuelaing.edu.co")
                .userType("STUDENT")
                .build();

        when(userUseCase.getUser(userId)).thenReturn(student);
        when(userMapper.toAuthResponse(student)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUser(userId);

        assertNotNull(result);
        assertEquals("STUDENT", result.getUserType());
        verify(userUseCase).getUser(userId);
        verify(userMapper).toAuthResponse(student);
    }

    @Test
    void givenAdminId_whenGetUser_thenReturnsMappedAuthResponse() {
        UUID adminId = UUID.randomUUID();
        Admin admin = new Admin();
        admin.setId(adminId);

        UserAuthResponse response = UserAuthResponse.builder()
                .id(admin.getId())
                .email("admin@escuelaing.edu.co")
                .userType("ADMIN")
                .build();

        when(userUseCase.getUser(adminId)).thenReturn(admin);
        when(userMapper.toAuthResponse(admin)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUser(adminId);

        assertNotNull(result);
        assertEquals("ADMIN", result.getUserType());
        verify(userUseCase).getUser(adminId);
    }

    // ── getUserByEmail ────────────────────────────────────────────

    @Test
    void givenStudentEmail_whenGetUserByEmail_thenReturnsMappedAuthResponse() {
        StudentProfile student = new StudentProfile();
        student.setId(UUID.randomUUID());

        UserAuthResponse response = UserAuthResponse.builder()
                .id(student.getId())
                .email("student@escuelaing.edu.co")
                .userType("STUDENT")
                .build();

        when(userUseCase.getUserByEmail("student@escuelaing.edu.co")).thenReturn(student);
        when(userMapper.toAuthResponse(student)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUserByEmail("student@escuelaing.edu.co");

        assertNotNull(result);
        assertEquals("STUDENT", result.getUserType());
        verify(userUseCase).getUserByEmail("student@escuelaing.edu.co");
    }

    @Test
    void givenOrganizerEmail_whenGetUserByEmail_thenReturnsMappedAuthResponse() {
        Organizer organizer = new Organizer();
        organizer.setId(UUID.randomUUID());

        UserAuthResponse response = UserAuthResponse.builder()
                .id(organizer.getId())
                .email("org@escuelaing.edu.co")
                .userType("ORGANIZER")
                .build();

        when(userUseCase.getUserByEmail("org@escuelaing.edu.co")).thenReturn(organizer);
        when(userMapper.toAuthResponse(organizer)).thenReturn(response);

        UserAuthResponse result = internalUserService.getUserByEmail("org@escuelaing.edu.co");

        assertNotNull(result);
        assertEquals("ORGANIZER", result.getUserType());
    }

    // ── verifyUser ────────────────────────────────────────────────

    @Test
    void givenUserId_whenVerifyUser_thenDelegatesToUseCase() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userUseCase).verifyUser(userId);

        internalUserService.verifyUser(userId);

        verify(userUseCase).verifyUser(userId);
    }

    // ── getProfileForMatching ───────────────────────────────────

    @Test
    void givenStudentForMatching_whenGetProfileForMatching_thenReturnsDtoWithFormattedSchedules() {
        UUID id = UUID.randomUUID();
        StudentProfile student = new StudentProfile();
        student.setId(id);

        // prepare response from mapper
        com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse resp = new com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse();
        resp.setId(id);
        resp.setCareer("Systems");
        resp.setSemester(3);
        resp.setTags(List.of("tag1","tag2"));

        com.matchpuff.profileservice.application.dto.response.ScheduleResponse s = new com.matchpuff.profileservice.application.dto.response.ScheduleResponse();
        s.setDayOfWeek(com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum.MONDAY);
        s.setName("Clase");
        s.setStartTime(java.time.LocalTime.of(10,0));
        s.setEndTime(java.time.LocalTime.of(12,30));
        resp.setSchedules(List.of(s));

        when(userUseCase.getUser(id)).thenReturn(student);
        when(userMapper.toUserMatchProfileResponseFromUser(student)).thenReturn(resp);

        var dto = internalUserService.getProfileForMatching(id);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(3, dto.getSemester());
        assertEquals(2, dto.getTags().size());
        assertNotNull(dto.getSchedulesAvailable());
        java.time.format.DateTimeFormatter tf = java.time.format.DateTimeFormatter.ofPattern("h:mma");
        String expectedStart = s.getStartTime().format(tf).replace(":00", "");
        String expectedEnd = s.getEndTime().format(tf).replace(":00", "");
        assertEquals("MONDAY_" + expectedStart + "-" + expectedEnd, dto.getSchedulesAvailable().get(0));
    }

    @Test
    void givenNonStudentForMatching_whenMapperReturnsNull_thenThrowsProfileServiceException() {
        UUID id = UUID.randomUUID();
        StudentProfile student = new StudentProfile();
        student.setId(id);

        when(userUseCase.getUser(id)).thenReturn(student);
        when(userMapper.toUserMatchProfileResponseFromUser(student)).thenReturn(null);

        assertThrows(com.matchpuff.profileservice.domain.exceptions.ProfileServiceException.class, () -> internalUserService.getProfileForMatching(id));
    }

    // ── getAllProfilesForMatching ───────────────────────────────

    @Test
    void getAllProfilesForMatching_mapsListAndFormatsSchedules() {
        StudentProfile s1 = new StudentProfile(); s1.setId(UUID.randomUUID());
        StudentProfile s2 = new StudentProfile(); s2.setId(UUID.randomUUID());

        com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse r1 = new com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse();
        r1.setId(s1.getId());
        r1.setCareer("C1");
        r1.setSemester(1);
        r1.setTags(List.of());
        r1.setSchedules(null);

        com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse r2 = new com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse();
        r2.setId(s2.getId());
        r2.setCareer("C2");
        r2.setSemester(2);
        com.matchpuff.profileservice.application.dto.response.ScheduleResponse sched = new com.matchpuff.profileservice.application.dto.response.ScheduleResponse();
        sched.setDayOfWeek(com.matchpuff.profileservice.domain.model.enums.DayOfWeekEnum.TUESDAY);
        sched.setStartTime(java.time.LocalTime.of(9,0));
        sched.setEndTime(java.time.LocalTime.of(11,0));
        r2.setSchedules(List.of(sched));

        when(userUseCase.getAllStudentProfiles()).thenReturn(List.of(s1,s2));
        when(userMapper.toUserMatchProfileResponseFromUser(s1)).thenReturn(r1);
        when(userMapper.toUserMatchProfileResponseFromUser(s2)).thenReturn(r2);

        var list = internalUserService.getAllProfilesForMatching();

        assertEquals(2, list.size());
        // first had null schedules -> dto schedulesAvailable null
        assertNull(list.get(0).getSchedulesAvailable());
        // second has one schedule formatted
        java.time.format.DateTimeFormatter tf2 = java.time.format.DateTimeFormatter.ofPattern("h:mma");
        String expStart = sched.getStartTime().format(tf2).replace(":00", "");
        String expEnd = sched.getEndTime().format(tf2).replace(":00", "");
        assertEquals("TUESDAY_" + expStart + "-" + expEnd, list.get(1).getSchedulesAvailable().get(0));
    }

    // ── getAllProfilesForMatching(requestingUserId) ───────────────────────────────

@Test
void givenRequestingUser_whenGetAllProfilesForMatching_thenExcludesSelfFriendsAndPrivateProfiles() {
    UUID requestingUserId = UUID.randomUUID();

    // self
    StudentProfile self = new StudentProfile();
    self.setId(requestingUserId);
    self.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PUBLIC);

    // friend
    StudentProfile friend = new StudentProfile();
    UUID friendId = UUID.randomUUID();
    friend.setId(friendId);
    friend.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PUBLIC);

    // private user
    StudentProfile privateUser = new StudentProfile();
    privateUser.setId(UUID.randomUUID());
    privateUser.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PRIVATE);

    // valid user
    StudentProfile valid = new StudentProfile();
    UUID validId = UUID.randomUUID();
    valid.setId(validId);
    valid.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PUBLIC);
    valid.setActive(true);

    // mapper response
    com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse resp =
            new com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse();

    resp.setId(validId);
    resp.setCareer("Systems");
    resp.setSemester(5);
    resp.setTags(List.of("java"));

    when(userUseCase.getUserFriends(requestingUserId))
            .thenReturn(List.of(friendId));

    when(userUseCase.getAllStudentProfiles())
            .thenReturn(List.of(self, friend, privateUser, valid));

    when(userMapper.toUserMatchProfileResponseFromUser(valid))
            .thenReturn(resp);

    var result = internalUserService.getAllProfilesForMatching(requestingUserId);

    assertEquals(1, result.size());

    var dto = result.get(0);

    assertEquals(validId, dto.getId());
    assertEquals("Systems", dto.getCareer());
    assertEquals(5, dto.getSemester());
    assertTrue(dto.isActive());

    verify(userUseCase).getUserFriends(requestingUserId);
    verify(userUseCase).getAllStudentProfiles();
    verify(userMapper).toUserMatchProfileResponseFromUser(valid);

    verify(userMapper, never()).toUserMatchProfileResponseFromUser(self);
    verify(userMapper, never()).toUserMatchProfileResponseFromUser(friend);
    verify(userMapper, never()).toUserMatchProfileResponseFromUser(privateUser);
}

@Test
void givenMapperReturnsNull_whenGetAllProfilesForMatching_thenNullDtosAreFiltered() {
    UUID requestingUserId = UUID.randomUUID();

    StudentProfile valid = new StudentProfile();
    valid.setId(UUID.randomUUID());
    valid.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PUBLIC);

    when(userUseCase.getUserFriends(requestingUserId))
            .thenReturn(List.of());

    when(userUseCase.getAllStudentProfiles())
            .thenReturn(List.of(valid));

    when(userMapper.toUserMatchProfileResponseFromUser(valid))
            .thenReturn(null);

    var result = internalUserService.getAllProfilesForMatching(requestingUserId);

    assertNotNull(result);
    assertTrue(result.isEmpty());

    verify(userMapper).toUserMatchProfileResponseFromUser(valid);
}

@Test
void givenValidProfiles_whenGetAllProfilesForMatching_thenSetsActiveFlagCorrectly() {
    UUID requestingUserId = UUID.randomUUID();

    StudentProfile activeStudent = new StudentProfile();
    activeStudent.setId(UUID.randomUUID());
    activeStudent.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PUBLIC);
    activeStudent.setActive(true);

    StudentProfile inactiveStudent = new StudentProfile();
    inactiveStudent.setId(UUID.randomUUID());
    inactiveStudent.setPrivacyLevel(com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum.PUBLIC);
    inactiveStudent.setActive(false);

    var activeResp = new com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse();
    activeResp.setId(activeStudent.getId());

    var inactiveResp = new com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse();
    inactiveResp.setId(inactiveStudent.getId());

    when(userUseCase.getUserFriends(requestingUserId))
            .thenReturn(List.of());

    when(userUseCase.getAllStudentProfiles())
            .thenReturn(List.of(activeStudent, inactiveStudent));

    when(userMapper.toUserMatchProfileResponseFromUser(activeStudent))
            .thenReturn(activeResp);

    when(userMapper.toUserMatchProfileResponseFromUser(inactiveStudent))
            .thenReturn(inactiveResp);

    var result = internalUserService.getAllProfilesForMatching(requestingUserId);

    assertEquals(2, result.size());

    assertTrue(result.get(0).isActive());
    assertFalse(result.get(1).isActive());
}
}
