package com.matchpuff.profileservice.application.usecase;

import com.matchpuff.profileservice.domain.ports.in.UserUseCasePort;
import com.matchpuff.profileservice.domain.exceptions.InvalidImageInputException;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.exceptions.ProfileServiceException;
import com.matchpuff.profileservice.domain.model.CategoryWithTags;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.*;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.ports.out.FriendshipEventPublisherPort;
import com.matchpuff.profileservice.domain.ports.out.ImageStoragePort;
import com.matchpuff.profileservice.domain.ports.out.OtpNotificationPort;
import com.matchpuff.profileservice.domain.ports.out.TagCatalogPort;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import com.matchpuff.profileservice.application.service.PasswordHashingService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
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
    private final TagCatalogPort tagCatalogPort;
    private final FriendshipEventPublisherPort friendshipEventPublisher;
    private final OtpNotificationPort otpNotificationPort;

    // ── CREATE ───────────────────────────────────────────────────

    @Override
    public User createStudentUser(StudentProfile student) {
        hashPasswordIfPresent(student);
        User saved = userRepository.save(student);
        otpNotificationPort.sendOtp(saved.getEmail());
        return saved;
    }

    @Override
    public User createAdminUser(Admin admin) {
        hashPasswordIfPresent(admin);
        User saved = userRepository.save(admin);
        otpNotificationPort.sendOtp(saved.getEmail());
        return saved;
    }

    @Override
    public User createOrganizerUser(Organizer organizer) {
        hashPasswordIfPresent(organizer);
        User saved = userRepository.save(organizer);
        otpNotificationPort.sendOtp(saved.getEmail());
        return saved;
    }

    // ── DELETE ───────────────────────────────────────────────────
    @Override
    public void deleteUser(UUID userId) {
        User user = findOrThrow(userId);

        if (user instanceof StudentProfile student && student.getFriendsId() != null) {
            for (UUID friendId : List.copyOf(student.getFriendsId())) {
                User friendUser = userRepository.findById(friendId).orElse(null);
                if (friendUser instanceof StudentProfile friendStudent
                        && friendStudent.getFriendsId() != null
                        && friendStudent.getFriendsId().contains(userId)) {
                    ensureMutableFriendsList(friendStudent);
                    friendStudent.getFriendsId().removeIf(f -> f.equals(userId));
                    userRepository.update(friendId, friendStudent);
                }
            }
        }

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

    @Override
    public List<UUID> getUserTags(UUID userId) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have tags", HttpStatus.BAD_REQUEST);
        }
        return student.getTagsId() != null ? student.getTagsId() : new ArrayList<>();
    }

    @Override
    public List<String> getUserTagsNames(UUID userId) {
        return getUserTags(userId).stream()
                .map(tagCatalogPort::getTagNameById)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<UUID> getUserFriends(UUID userId) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have friends", HttpStatus.BAD_REQUEST);
        }
        return student.getFriendsId() != null ? student.getFriendsId() : new ArrayList<>();
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
    public void resetPassword(UUID userId, String newPassword) {
        User user = findOrThrow(userId);
        user.setPasswordHash(passwordHashingService.hashPassword(newPassword));
        userRepository.update(userId, user);
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
        if (request.getGender() != null)       student.setGender(request.getGender());
        if (request.getBiography() != null)    student.setBiography(request.getBiography());
        if (request.getStudentCarnet() != null) student.setStudentCarnet(request.getStudentCarnet());
        if (request.getPrivacyLevel() != null) student.setPrivacyLevel(request.getPrivacyLevel());
        if (request.getCareer() != null)       student.setCareer(request.getCareer());
        if (request.getSemester() != null)      student.setSemester(request.getSemester());
        if (request.getDateOfBirth() != null)  student.setDateOfBirth(request.getDateOfBirth());
        if (request.getTagsId() != null)         student.setTagsId(request.getTagsId());
        if (request.getSchedulesAvailability() != null) {
            validateNoOverlapsInList(request.getSchedulesAvailability());
            student.setSchedulesAvailability(request.getSchedulesAvailability());
        }

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
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have schedules", HttpStatus.BAD_REQUEST);
        }
        if (!(student.getSchedulesAvailability() instanceof java.util.ArrayList)) {
            student.setSchedulesAvailability(student.getSchedulesAvailability() == null ? new ArrayList<>() : new ArrayList<>(student.getSchedulesAvailability()));
        }
        validateNoOverlap(schedule, student.getSchedulesAvailability());
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
        if (!(student.getSchedulesAvailability() instanceof ArrayList)) {
            student.setSchedulesAvailability(new ArrayList<>(student.getSchedulesAvailability()));
        }
        boolean removed = student.getSchedulesAvailability().removeIf(s -> s.equals(schedule));
        if (!removed) {
            throw new ProfileServiceException("Schedule not found for removal", HttpStatus.BAD_REQUEST);
        }
        return userRepository.update(userId, student);
    }

    @Override
    public User addFriendToStudent(UUID userId, UUID friendId) {
        if (userId.equals(friendId)) {
            throw new InvalidInputException("A user cannot add themselves as a friend");
        }

        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new ProfileServiceException("Friend user not found: " + friendId, HttpStatus.NOT_FOUND));
        if (!(friendUser instanceof StudentProfile friendStudent)) {
            throw new InvalidInputException("Only STUDENT users can be added as friends");
        }

        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have friends", HttpStatus.BAD_REQUEST);
        }
        if (student.getFriendsId() != null && student.getFriendsId().contains(friendId)) {
            throw new InvalidInputException("This user is already a friend");
        }

        ensureMutableFriendsList(student);
        ensureMutableFriendsList(friendStudent);

        // Update userId's list first
        student.getFriendsId().add(friendId);
        userRepository.update(userId, student);

        // Update friendId's list; compensate if it fails
        try {
            friendStudent.getFriendsId().add(userId);
            userRepository.update(friendId, friendStudent);
        } catch (Exception e) {
            student.getFriendsId().removeIf(f -> f.equals(friendId));
            userRepository.update(userId, student);
            throw new ProfileServiceException("Could not add friend on the other side. Operation was reverted.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        friendshipEventPublisher.publishFriendshipCreated(userId, friendId);

        return student;
    }

    @Override
    public User removeFriendFromStudent(UUID userId, UUID friendId) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have friends", HttpStatus.BAD_REQUEST);
        }
        if (student.getFriendsId() == null) {
            throw new ProfileServiceException("No friends to remove", HttpStatus.BAD_REQUEST);
        }
        ensureMutableFriendsList(student);
        boolean removed = student.getFriendsId().removeIf(f -> f.equals(friendId));
        if (!removed) {
            throw new ProfileServiceException("Friend not found for removal", HttpStatus.BAD_REQUEST);
        }
        userRepository.update(userId, student);

        // Bidirectional: also remove userId from friend's list; compensate if it fails
        User friendUser = userRepository.findById(friendId).orElse(null);
        if (friendUser instanceof StudentProfile friendStudent
                && friendStudent.getFriendsId() != null
                && friendStudent.getFriendsId().contains(userId)) {
            ensureMutableFriendsList(friendStudent);
            try {
                friendStudent.getFriendsId().removeIf(f -> f.equals(userId));
                userRepository.update(friendId, friendStudent);
            } catch (Exception e) {
                student.getFriendsId().add(friendId);
                userRepository.update(userId, student);
                throw new ProfileServiceException("Could not remove friend on the other side. Operation was reverted.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return student;
    }

    private void validateNoOverlap(Schedule newSchedule, List<Schedule> existing) {
        for (Schedule s : existing) {
            if (s.getDayOfWeek() == newSchedule.getDayOfWeek()
                    && s.getStartTime().isBefore(newSchedule.getEndTime())
                    && newSchedule.getStartTime().isBefore(s.getEndTime())) {
                throw new InvalidInputException(
                    "Schedule overlaps with an existing schedule on " + newSchedule.getDayOfWeek());
            }
        }
    }

    private void validateNoOverlapsInList(List<Schedule> schedules) {
        for (int i = 0; i < schedules.size(); i++) {
            for (int j = i + 1; j < schedules.size(); j++) {
                Schedule a = schedules.get(i);
                Schedule b = schedules.get(j);
                if (a.getDayOfWeek() == b.getDayOfWeek()
                        && a.getStartTime().isBefore(b.getEndTime())
                        && b.getStartTime().isBefore(a.getEndTime())) {
                    throw new InvalidInputException("Schedules overlap on " + a.getDayOfWeek());
                }
            }
        }
    }

    private void ensureMutableFriendsList(StudentProfile student) {
        if (!(student.getFriendsId() instanceof ArrayList)) {
            student.setFriendsId(student.getFriendsId() == null ? new ArrayList<>() : new ArrayList<>(student.getFriendsId()));
        }
    }

    @Override
    public User addTagToStudent(UUID userId, UUID tagId) {
        if (!tagCatalogPort.tagExists(tagId)) {
            throw new InvalidInputException("Tag not found in the catalog: " + tagId);
        }
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users can have tags", HttpStatus.BAD_REQUEST);
        }
        if (!(student.getTagsId() instanceof java.util.ArrayList)) {
            student.setTagsId(student.getTagsId() == null ? new ArrayList<>() : new ArrayList<>(student.getTagsId()));
        }
        if (student.getTagsId().contains(tagId)) {
            throw new ProfileServiceException("Tag already assigned to this student", HttpStatus.CONFLICT);
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
        if (!(student.getTagsId() instanceof java.util.ArrayList)) {
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

    @Override
    public List<CategoryWithTags> getTagCatalog() {
        return tagCatalogPort.getAllCategoriesWithTags();
    }

    @Override
    public List<User> getUsersByIds(List<UUID> ids) {
        return userRepository.findAllByIds(ids);
    }

    @Override
    public User updateXp(UUID userId, int xp) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users have XP", HttpStatus.BAD_REQUEST);
        }
        student.setXp(xp);
        return userRepository.update(userId, student);
    }

    @Override
    public User updateLevel(UUID userId, int level) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users have levels", HttpStatus.BAD_REQUEST);
        }
        student.setLevel(level);
        return userRepository.update(userId, student);
    }

    @Override
    public User updateActiveStatus(UUID userId, boolean active) {
        User user = findOrThrow(userId);
        if (!(user instanceof StudentProfile student)) {
            throw new ProfileServiceException("Only STUDENT users have active status", HttpStatus.BAD_REQUEST);
        }
        student.setActive(active);
        return userRepository.update(userId, student);
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
