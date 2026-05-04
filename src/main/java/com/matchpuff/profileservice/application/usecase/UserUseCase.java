package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.domain.exceptions.InvalidImageInputException;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.*;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {
    private final UserRepositoryPort userRepository;
    private final ImageStoragePort imageStoragePort;

    // ── CREATE ───────────────────────────────────────────────────

    @Override
    public User createStudentUser(StudentProfile student) {
        return userRepository.save(student);
    }

    @Override
    public User createAdminUser(Admin admin) {
        return userRepository.save(admin);
    }

    @Override
    public User createOrganizerUser(Organizer organizer) {
        return userRepository.save(organizer);
    }

    // ── DELETE ───────────────────────────────────────────────────
    @Override
    public void deleteUser(String userId) {
        findOrThrow(userId);
        userRepository.delete(userId);
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

        return userRepository.update(userId, student);
    }

    @Override
    public User addScheduleToStudent(String userId, Schedule schedule) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);
        if (student.getSchedules() == null) {
            student.setSchedules(new java.util.ArrayList<>());
        }
        student.getSchedules().add(schedule);
        return userRepository.update(userId, student);
    }

    @Override
    public User addTagToStudent(String userId, Tag tag) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);
        if (student.getTags() == null) {
            student.setTags(new java.util.ArrayList<>());
        }
        student.getTags().add(tag);
        return userRepository.update(userId, student);
    }

    @Override
    public User updateProfileImage(String userId, byte[] file, String contentType) {
        if (file == null || file.length == 0) throw new InvalidImageInputException("The file is empty");
        if (file.length > 5 * 1024 * 1024) throw new InvalidImageInputException("Image exceeds 5MB limit");
        if (!"image/png".equals(contentType) && !"image/jpeg".equals(contentType)) throw new InvalidImageInputException("Invalid format. Only PNG and JPEG are allowed");

        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) throw new ProfileServiceException("Only STUDENT users can update profile images", HttpStatus.BAD_REQUEST);

        String imageUrl = imageStoragePort.uploadProfileImage(file, userId);
        student.setPhotoUrl(imageUrl);
        return userRepository.update(userId, student);
    }

    // ── GET ALL ─────────────────────────────────────────────────
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<StudentProfile> getAllStudentProfiles() {
        return userRepository.findAllStudents();
    }

    // ── Helpers ──────────────────────────────────────────────────

    private User findOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProfileServiceException("User not found: " + userId, HttpStatus.NOT_FOUND));
    }
}
