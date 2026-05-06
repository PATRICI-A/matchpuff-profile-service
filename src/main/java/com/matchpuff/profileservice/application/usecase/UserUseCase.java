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
import com.matchpuff.profileservice.application.service.PasswordHashingService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserUseCase implements UserUseCasePort {
    private final UserRepositoryPort userRepository;
    private final ImageStoragePort imageStoragePort;
    private final PasswordHashingService passwordHashingService;

    // ── CREATE ───────────────────────────────────────────────────

    @Override
    public User createStudentUser(StudentProfile student) {
        hashPasswordIfPresent(student);
        return userRepository.save(student);
    }

    @Override
    public User createAdminUser(Admin admin) {
        hashPasswordIfPresent(admin);
        return userRepository.save(admin);
    }

    @Override
    public User createOrganizerUser(Organizer organizer) {
        hashPasswordIfPresent(organizer);
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
    public User updateUser(String userId, User user) {
        if (user instanceof StudentProfile student) {
            return updateStudentUser(userId, student);
        }

        if (user instanceof Admin admin) {
            return updateAdminUser(userId, admin);
        }

        if (user instanceof Organizer organizer) {
            return updateOrganizerUser(userId, organizer);
        }

        throw new ProfileServiceException("Unsupported user type for update", HttpStatus.BAD_REQUEST);
    }

    @Override
    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = findOrThrow(userId);

        if (user.getPasswordHash() == null || !passwordHashingService.verifyPassword(currentPassword, user.getPasswordHash())) {
            throw new ProfileServiceException("Current password is invalid", HttpStatus.BAD_REQUEST);
        }

        user.setPasswordHash(passwordHashingService.hashPassword(newPassword));
        userRepository.update(userId, user);
    }

    public User updateStudentUser(String userId, StudentProfile request) {
        StudentProfile student = (StudentProfile) findOrThrow(userId);

        if (request.getName() != null)         student.setName(request.getName());
        if (request.getEmail() != null)        student.setEmail(request.getEmail());
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            student.setPasswordHash(passwordHashingService.hashPassword(request.getPasswordHash()));
        }
        if (request.getBiography() != null)    student.setBiography(request.getBiography());
        if (request.getStudentCarnet() != null) student.setStudentCarnet(request.getStudentCarnet());
        if (request.getPrivacyLevel() != null) student.setPrivacyLevel(request.getPrivacyLevel());
        if (request.getCareer() != null)       student.setCareer(request.getCareer());
        if (request.getSemester() > 0)         student.setSemester(request.getSemester());
        if (request.getTags() != null)         student.setTags(request.getTags());
        if (request.getSchedules() != null)    student.setSchedules(request.getSchedules());

        return userRepository.update(userId, student);
    }

    public User updateAdminUser(String userId, Admin request) {
        Admin admin = (Admin) findOrThrow(userId);

        if (request.getName() != null)         admin.setName(request.getName());
        if (request.getEmail() != null)        admin.setEmail(request.getEmail());
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            admin.setPasswordHash(passwordHashingService.hashPassword(request.getPasswordHash()));
        }
        if (request.getGender() != null)       admin.setGender(request.getGender());

        return userRepository.update(userId, admin);
    }

    public User updateOrganizerUser(String userId, Organizer request) {
        Organizer organizer = (Organizer) findOrThrow(userId);

        if (request.getName() != null)         organizer.setName(request.getName());
        if (request.getEmail() != null)        organizer.setEmail(request.getEmail());
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            organizer.setPasswordHash(passwordHashingService.hashPassword(request.getPasswordHash()));
        }
        if (request.getGender() != null)       organizer.setGender(request.getGender());
        if (request.getContactInfo() != null)  organizer.setContactInfo(request.getContactInfo());

        return userRepository.update(userId, organizer);
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

    private void hashPasswordIfPresent(User user) {
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            String hashedPassword = passwordHashingService.hashPassword(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);
        }
    }

    private User findOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProfileServiceException("User not found: " + userId, HttpStatus.NOT_FOUND));
    }
}
