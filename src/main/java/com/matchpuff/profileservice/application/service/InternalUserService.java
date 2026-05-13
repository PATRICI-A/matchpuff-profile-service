package com.matchpuff.profileservice.application.service;

import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.TagResponse;
import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileDto;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse;
import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.application.mapper.UserMapper;


@Service
@RequiredArgsConstructor
public class InternalUserService implements InternalUserServicePort {  //IMPORTANTE el caso de uso lo deberia manejar
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
    public UserMatchProfileDto getProfileForMatching(UUID id) {
        UserMatchProfileResponse resp = userMapper.toUserMatchProfileResponseFromUser(userUseCase.getUser(id));
        return convertToDto(resp);
    }

    @Override
    public List<UserMatchProfileDto> getAllProfilesForMatching() {
        return userUseCase.getAllUsers().stream()
                .map(userMapper::toUserMatchProfileResponseFromUser)
                .map(this::convertToDto)
                .toList();
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
