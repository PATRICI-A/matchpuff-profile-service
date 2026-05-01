package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.application.dto.request.RegisterRequestStudent;
import com.matchpuff.profileservice.application.dto.request.UserRequest;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.mapper.UserMapper;
import com.matchpuff.profileservice.application.service.UserServicePort;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserUseCase implements UserServicePort {
    private final UserRepository userRepository;

    // ── CREATE ───────────────────────────────────────────────────

    @Override
    public UserResponse createUser(UserRequest request) {
        StudentProfile student = new StudentProfile();
        student.setName(request.getName());
        student.setCareer(request.getCarreer());
        student.setSemester(request.getSemester());
        student.setBiography(request.getBiography());
        student.setPrivacyLevel(request.getPrivacyLevel());
        if (request.getTags() != null) student.setTags(toTagList(request.getTags()));
        if (request.getSchedules() != null)  student.setSchedules(toScheduleList(request.getSchedules()));

        return UserMapper.toResponse(userRepository.save(student));
    }

    // ── GET ──────────────────────────────────────────────────────

    @Override
    public UserResponse getUser(String userId) {
        return UserMapper.toResponse(findOrThrow(userId));
    }

    // ── UPDATE ───────────────────────────────────────────────────

    @Override
    public UserResponse updateUser(String userId, UserRequest request) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);

        if (request.getName() != null)         student.setName(request.getName());
        if (request.getBiography() != null)    student.setBiography(request.getBiography());
        if (request.getPrivacyLevel() != null) student.setPrivacyLevel(request.getPrivacyLevel());
        if (request.getTags() != null)    student.setTags(toTagList(request.getTags()));
        if (request.getSchedules() != null)     student.setSchedules(toScheduleList(request.getSchedules()));

        return UserMapper.toResponse(userRepository.save(student));
    }

    // ── Helpers ──────────────────────────────────────────────────

    private User findOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProfileServiceException("Usuario no encontrado: " + userId, HttpStatus.NOT_FOUND));
    }

    private List<Tag> toTagList(List<Tag> names) {
        return names.stream().map(name -> {
            Tag t = new Tag();
            t.setName(String.valueOf(name));
            return t;
        }).collect(Collectors.toList());
    }

    private List<Schedule> toScheduleList(List<Schedule> schedules) {
        return schedules.stream().map(s -> {
            Schedule sc = new Schedule();
            sc.setDayOfWeek(s.getDayOfWeek());
            sc.setName(s.getName());
            sc.setStartTime(s.getStartTime());
            sc.setEndTime(s.getEndTime());
            return sc;
        }).collect(Collectors.toList());
    }

}
