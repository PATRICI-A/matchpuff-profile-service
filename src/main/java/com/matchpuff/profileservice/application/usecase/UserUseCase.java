package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.domain.exceptions.InvalidImageInputException;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.*;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import com.matchpuff.profileservice.application.service.PasswordHashingService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


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
    public void deleteUser(UUID userId) {
        findOrThrow(userId);
        userRepository.delete(userId);
    }

    // ── GET ──────────────────────────────────────────────────────

    @Override
    public User getUser(UUID userId) {
        return findOrThrow(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ProfileServiceException("User not found with email: " + email, HttpStatus.NOT_FOUND));
    }

    // ── UPDATE ───────────────────────────────────────────────────
    @Override
    public void verifyUser(UUID userId) {
        User user = findOrThrow(userId);
        if (user.isVerified()) {
            throw new ProfileServiceException("User is already verified", HttpStatus.BAD_REQUEST);
        }
        user.setVerified(true);
        userRepository.update(userId, user);
    }
    
    @Override
    public User updateGeolocation(UUID userId, boolean geolocationEnabled) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile)) {
            throw new ProfileServiceException("Only STUDENT users can update geolocation settings", HttpStatus.BAD_REQUEST);
        }
        ((StudentProfile) user).setGeolocationEnabled(geolocationEnabled);
        return userRepository.update(userId, user);
    }

    @Override
    public User updateUser(UUID userId, User user) {
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
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = findOrThrow(userId);

        if (user.getPasswordHash() == null || !passwordHashingService.verifyPassword(currentPassword, user.getPasswordHash())) {
            throw new ProfileServiceException("Current password is invalid", HttpStatus.BAD_REQUEST);
        }

        user.setPasswordHash(passwordHashingService.hashPassword(newPassword));
        userRepository.update(userId, user);
    }

    public User updateStudentUser(UUID userId, StudentProfile request) {
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
        if (request.getDateOfBirth() != null)  student.setDateOfBirth(request.getDateOfBirth());
        if (request.getTagsId() != null)         student.setTagsId(request.getTagsId());
        if (request.getSchedulesAvailability() != null)    student.setSchedulesAvailability(request.getSchedulesAvailability());

        return userRepository.update(userId, student);
    }

    public User updateAdminUser(UUID userId, Admin request) {
        Admin admin = (Admin) findOrThrow(userId);

        if (request.getName() != null)         admin.setName(request.getName());
        if (request.getEmail() != null)        admin.setEmail(request.getEmail());
        if (request.getPasswordHash() != null && !request.getPasswordHash().isEmpty()) {
            admin.setPasswordHash(passwordHashingService.hashPassword(request.getPasswordHash()));
        }
        if (request.getGender() != null)       admin.setGender(request.getGender());

        return userRepository.update(userId, admin);
    }

    public User updateOrganizerUser(UUID userId, Organizer request) {
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
    public User addScheduleToStudent(UUID userId, Schedule schedule) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile)) {
            throw new ProfileServiceException("Only STUDENT users can have schedules", HttpStatus.BAD_REQUEST);
        }
        StudentProfile student = (StudentProfile) findOrThrow(userId);
        if (student.getSchedulesAvailability() == null || student.getSchedulesAvailability().getClass().getName().contains("ImmutableCollections")) {
            student.setSchedulesAvailability(student.getSchedulesAvailability() == null ? new ArrayList<>() : new ArrayList<>(student.getSchedulesAvailability()));
        }
        student.getSchedulesAvailability().add(schedule);
        return userRepository.update(userId, student);
    }

    @Override
    public User removeScheduleFromStudent(UUID userId, Schedule schedule) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have schedules", HttpStatus.BAD_REQUEST);
        }
        if (student.getSchedulesAvailability() == null) {
            throw new ProfileServiceException("No schedules to remove", HttpStatus.BAD_REQUEST);
        }
        if (student.getSchedulesAvailability().getClass().getName().contains("ImmutableCollections")) {
            student.setSchedulesAvailability(new ArrayList<>(student.getSchedulesAvailability()));
        }
        boolean removed = student.getSchedulesAvailability().removeIf(s -> s.equals(schedule));
        if (!removed) {
            throw new ProfileServiceException("Schedule not found for removal", HttpStatus.BAD_REQUEST);
        }
        return userRepository.update(userId, student);
    }

    @Override
    public User addTagToStudent(UUID userId, UUID tagId) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile)) {
            throw new ProfileServiceException("Only STUDENT users can have tags", HttpStatus.BAD_REQUEST);
        }
        StudentProfile student = (StudentProfile) findOrThrow(userId);
        if (student.getTagsId() == null || student.getTagsId().getClass().getName().contains("ImmutableCollections")) {
            student.setTagsId(student.getTagsId() == null ? new ArrayList<>() : new ArrayList<>(student.getTagsId()));
        }
        student.getTagsId().add(tagId);
        return userRepository.update(userId, student);
    }

    @Override
    public User removeTagFromStudent(UUID userId, UUID tagId) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have tags", HttpStatus.BAD_REQUEST);
        }
        if (student.getTagsId() == null) {
            throw new ProfileServiceException("No tags to remove", HttpStatus.BAD_REQUEST);
        }
        if (student.getTagsId().getClass().getName().contains("ImmutableCollections")) {
            student.setTagsId(new ArrayList<>(student.getTagsId()));
        }
        boolean removed = student.getTagsId().removeIf(t -> t.equals(tagId));
        if (!removed) {
            throw new ProfileServiceException("Tag not found for removal", HttpStatus.BAD_REQUEST);
        }
        return userRepository.update(userId, student);
    }

    @Override
    public User updateProfileImage(UUID userId, byte[] file, String contentType) {
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

    private User findOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProfileServiceException("User not found: " + userId, HttpStatus.NOT_FOUND));
    }
}
