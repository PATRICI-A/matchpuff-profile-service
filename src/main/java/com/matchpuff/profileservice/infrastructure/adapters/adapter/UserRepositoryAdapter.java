package com.matchpuff.profileservice.infrastructure.adapters.adapter;

import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper.UserPersistenceMapper;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;

    public UserRepositoryAdapter(UserRepository userRepository, UserPersistenceMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(StudentProfile student) {
        mailExistsForException(student.getEmail());
        StudentProfileDocument doc = userMapper.toDocument(student);
        ensureCreatedAt(doc);
        StudentProfileDocument saved = userRepository.save(doc);
        return userMapper.toDomain(saved);
    }

    @Override
    public User save(Admin admin) {
        mailExistsForException(admin.getEmail());
        AdminProfileDocument doc = userMapper.toDocument(admin);
        ensureCreatedAt(doc);
        AdminProfileDocument saved = userRepository.save(doc);
        return userMapper.toDomain(saved);
    }

    @Override
    public User save(Organizer organizer) {
        mailExistsForException(organizer.getEmail());
        OrganizerProfileDocument doc = userMapper.toDocument(organizer);
        ensureCreatedAt(doc);
        OrganizerProfileDocument saved = userRepository.save(doc);
        return userMapper.toDomain(saved);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .flatMap(userMapper::toDomainByType);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(userMapper::toDomainByType);
    }

    @Override
    public User update(UUID id, User user) {

        if (user.getEmail() != null) {
            Optional<UserDocument> existing = userRepository.findByEmail(user.getEmail());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new InvalidInputException("The email is already in use by another user.");
            }
        }

        UserDocument storedUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("User not found: " + id));

        if (user instanceof StudentProfile student) {
            if (!(storedUser instanceof StudentProfileDocument current)) {
                throw new InvalidInputException("Unsupported user type for update.");
            }

            StudentProfileDocument doc = mergeStudentDocument(current, student);
            StudentProfileDocument updated = userRepository.save(doc);
            return userMapper.toDomain(updated);
        }

        if (user instanceof Admin admin) {
            if (!(storedUser instanceof AdminProfileDocument current)) {
                throw new InvalidInputException("Unsupported user type for update.");
            }

            AdminProfileDocument doc = mergeAdminDocument(current, admin);
            AdminProfileDocument updated = userRepository.save(doc);
            return userMapper.toDomain(updated);
        }

        if (user instanceof Organizer organizer) {
            if (!(storedUser instanceof OrganizerProfileDocument current)) {
                throw new InvalidInputException("Unsupported user type for update.");
            }

            OrganizerProfileDocument doc = mergeOrganizerDocument(current, organizer);
            OrganizerProfileDocument updated = userRepository.save(doc);
            return userMapper.toDomain(updated);
        }

        throw new InvalidInputException("Unsupported user type for update.");
    }

    private StudentProfileDocument mergeStudentDocument(StudentProfileDocument current, StudentProfile incoming) {
        StudentProfileDocument merged = new StudentProfileDocument();
        merged.setId(current.getId());
        merged.setUserType(current.getUserType());
        merged.setCreatedAt(current.getCreatedAt());
        merged.setName(incoming.getName() != null ? incoming.getName() : current.getName());
        merged.setEmail(incoming.getEmail() != null ? incoming.getEmail() : current.getEmail());
        merged.setPasswordHash(incoming.getPasswordHash() != null ? incoming.getPasswordHash() : current.getPasswordHash());
        merged.setGender(incoming.getGender() != null ? incoming.getGender() : current.getGender());
        merged.setBirthdate(incoming.getDateOfBirth() != null ? incoming.getDateOfBirth().atStartOfDay() : current.getBirthdate());
        merged.setPhotourl(incoming.getPhotoUrl() != null ? incoming.getPhotoUrl() : current.getPhotourl());
        merged.setCareer(incoming.getCareer() != null ? incoming.getCareer() : current.getCareer());
        merged.setSemester(incoming.getSemester() > 0 ? incoming.getSemester() : current.getSemester());
        merged.setStudentCarnet(incoming.getStudentCarnet() != null ? incoming.getStudentCarnet() : current.getStudentCarnet());
        merged.setBiography(incoming.getBiography() != null ? incoming.getBiography() : current.getBiography());
        merged.setPrivacyLevel(incoming.getPrivacyLevel() != null ? incoming.getPrivacyLevel() : current.getPrivacyLevel());
        merged.setGeolocationEnabled(incoming.isGeolocationEnabled());
        merged.setSchedule(incoming.getSchedules() != null ? userMapper.toDocument(incoming).getSchedule() : current.getSchedule());
        merged.setTagsId(incoming.getTagsId() != null ? incoming.getTagsId() : current.getTagsId());
        return merged;
    }

    private AdminProfileDocument mergeAdminDocument(AdminProfileDocument current, Admin incoming) {
        AdminProfileDocument merged = new AdminProfileDocument();
        merged.setId(current.getId());
        merged.setUserType(current.getUserType());
        merged.setCreatedAt(current.getCreatedAt());
        merged.setName(incoming.getName() != null ? incoming.getName() : current.getName());
        merged.setEmail(incoming.getEmail() != null ? incoming.getEmail() : current.getEmail());
        merged.setPasswordHash(incoming.getPasswordHash() != null ? incoming.getPasswordHash() : current.getPasswordHash());
        merged.setGender(incoming.getGender() != null ? incoming.getGender() : current.getGender());
        return merged;
    }

    private OrganizerProfileDocument mergeOrganizerDocument(OrganizerProfileDocument current, Organizer incoming) {
        OrganizerProfileDocument merged = new OrganizerProfileDocument();
        merged.setId(current.getId());
        merged.setUserType(current.getUserType());
        merged.setCreatedAt(current.getCreatedAt());
        merged.setName(incoming.getName() != null ? incoming.getName() : current.getName());
        merged.setEmail(incoming.getEmail() != null ? incoming.getEmail() : current.getEmail());
        merged.setPasswordHash(incoming.getPasswordHash() != null ? incoming.getPasswordHash() : current.getPasswordHash());
        merged.setGender(incoming.getGender() != null ? incoming.getGender() : current.getGender());
        merged.setContact(incoming.getContactInfo() != null ? incoming.getContactInfo() : current.getContact());
        return merged;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .flatMap(doc -> userMapper.toDomainByType(doc).stream())
                .toList();
    }

    @Override
    public List<StudentProfile> findAllStudents() {
        return userRepository.findByUserType(UserType.STUDENT).stream()
                .flatMap(doc -> userMapper.toDomainByType(doc).stream())
                .filter(StudentProfile.class::isInstance)
                .map(StudentProfile.class::cast)
                .toList();
    }

    private boolean mailExistsForException(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidInputException("The email is already in use by another user.");
        }
        return false;
    }

    private void ensureCreatedAt(UserDocument userDocument) {
        if (userDocument.getCreatedAt() == null) {
            userDocument.setCreatedAt(LocalDateTime.now());
        }
    }
}
