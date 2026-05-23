package com.matchpuff.profileservice.application.service;

import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileDto;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.enums.PrivacyLevelEnum;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.application.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class InternalUserService implements InternalUserServicePort {
    private final UserUseCasePort userUseCase;
    private final UserMapper userMapper;

    @Override
    public UserAuthResponse getUser(UUID userId) {
        return userMapper.toAuthResponse(userUseCase.getUser(userId));
    }

    @Override
    public UserAuthResponse getUserByEmail(String email) {
        return userMapper.toAuthResponse(userUseCase.getUserByEmail(email));
    }

    @Override
    public void verifyUser(UUID userId) {
        userUseCase.verifyUser(userId);
    }

    @Override
    public void resetPassword(UUID userId, String newPassword) {
        userUseCase.resetPassword(userId, newPassword);
    }

    @Override
    public UserMatchProfileDto getProfileForMatching(UUID id) {
        UserMatchProfileResponse resp = userMapper.toUserMatchProfileResponseFromUser(userUseCase.getUser(id));
        if (resp == null) {
            throw new ProfileServiceException("Only student profiles are available for matching", HttpStatus.BAD_REQUEST);
        }
        return convertToDto(resp);
    }

    @Override
    public List<UserMatchProfileDto> getAllProfilesForMatching() {
        return userUseCase.getAllStudentProfiles().stream()
                .filter(s -> s.getPrivacyLevel() != PrivacyLevelEnum.PRIVATE)
                .map(s -> {
                    UserMatchProfileDto dto = convertToDto(userMapper.toUserMatchProfileResponseFromUser(s));
                    if (dto != null) dto.setActive(s.isActive());
                    return dto;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<UserMatchProfileDto> getAllProfilesForMatching(UUID requestingUserId) {
        Set<UUID> friends = Set.copyOf(userUseCase.getUserFriends(requestingUserId));
        return userUseCase.getAllStudentProfiles().stream()
                .filter(s -> !s.getId().equals(requestingUserId))
                .filter(s -> !friends.contains(s.getId()))
                .filter(s -> s.getPrivacyLevel() != PrivacyLevelEnum.PRIVATE)
                .map(s -> {
                    UserMatchProfileDto dto = convertToDto(userMapper.toUserMatchProfileResponseFromUser(s));
                    if (dto != null) dto.setActive(s.isActive());
                    return dto;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public boolean isGeolocationEnabled(UUID userId) {
        var user = userUseCase.getUser(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users have geolocation settings", HttpStatus.BAD_REQUEST);
        }
        return student.isGeolocationEnabled();
    }

    @Override
    public boolean isActive(UUID userId) {
        var user = userUseCase.getUser(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users have active status", HttpStatus.BAD_REQUEST);
        }
        return student.isActive();
    }

    private UserMatchProfileDto convertToDto(UserMatchProfileResponse resp) {
        if (resp == null) return null;
        UserMatchProfileDto dto = new UserMatchProfileDto();
        dto.setId(resp.getId());
        dto.setCareer(resp.getCareer());
        dto.setSemester(resp.getSemester());
        dto.setTags( resp.getTags());
        
        List<ScheduleResponse> schedules = resp.getSchedules();
        if (schedules == null) {
            dto.setSchedulesAvailable(null);
        } else {
            DateTimeFormatter tf = java.time.format.DateTimeFormatter.ofPattern("h:mma");
            List<String> formatted = schedules.stream()
                    .map(s -> s.getDayOfWeek().name() + "_" + formatTime(s.getStartTime(), tf) + "-" + formatTime(s.getEndTime(), tf))
                    .toList();
            dto.setSchedulesAvailable(formatted);
        }
        return dto;
    }

    private String formatTime(java.time.LocalTime t, java.time.format.DateTimeFormatter tf) {
        if (t == null) return "";
        String formatted = t.format(tf);
        formatted = formatted.replace(":00", "");
        return formatted;
    }

}
