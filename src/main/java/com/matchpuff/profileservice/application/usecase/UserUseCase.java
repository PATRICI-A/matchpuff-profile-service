package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {
    private final UserRepositoryPort userRepository;

    // ── CREATE ───────────────────────────────────────────────────

    @Override
    public User createStudentUser(StudentProfile student) {
        return userRepository.save(student);
    }

    // ── GET ──────────────────────────────────────────────────────

    @Override
    public User getUser(String userId) {
        return findOrThrow(userId);
    }

    // ── UPDATE ───────────────────────────────────────────────────

    @Override
    public User updateStudentUser(String userId, StudentProfile request) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);

        if (request.getName() != null)         student.setName(request.getName());
        if (request.getEmail() != null)        student.setEmail(request.getEmail());
        if (request.getBiography() != null)    student.setBiography(request.getBiography());
        if (request.getPrivacyLevel() != null) student.setPrivacyLevel(request.getPrivacyLevel());
        if (request.getCareer() != null)       student.setCareer(request.getCareer());
        if (request.getSemester() > 0)         student.setSemester(request.getSemester());
        if (request.getTags() != null)         student.setTags(request.getTags());
        if (request.getSchedules() != null)    student.setSchedules(request.getSchedules());

        return userRepository.save(student);
    }

    @Override
    public User addScheduleToStudent(String userId, Schedule schedule) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);
        if (student.getSchedules() == null) {
            student.setSchedules(new java.util.ArrayList<>());
        }
        student.getSchedules().add(schedule);
        return userRepository.save(student);
    }

    @Override
    public User addTagToStudent(String userId, Tag tag) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);
        if (student.getTags() == null) {
            student.setTags(new java.util.ArrayList<>());
        }
        student.getTags().add(tag);
        return userRepository.save(student);
    }

    // ── GET ALL ─────────────────────────────────────────────────
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ── Helpers ──────────────────────────────────────────────────

    private User findOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProfileServiceException("Usuario no encontrado: " + userId, HttpStatus.NOT_FOUND));
    }
}
