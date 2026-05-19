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

        if (user instanceof StudentProfile student) {
            return userMapper.toDomain(userRepository.save(userMapper.toDocument(student)));
        }

        if (user instanceof Admin admin) {
            return userMapper.toDomain(userRepository.save(userMapper.toDocument(admin)));
        }

        if (user instanceof Organizer organizer) {
            return userMapper.toDomain(userRepository.save(userMapper.toDocument(organizer)));
        }

        throw new InvalidInputException("Unsupported user type for update.");
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

    @Override
    public List<User> findAllByIds(List<UUID> ids) {
        return userRepository.findAllById(ids).stream()
                .flatMap(doc -> userMapper.toDomainByType(doc).stream())
                .toList();
    }

    private void mailExistsForException(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidInputException("The email is already in use by another user.");
        }
    }

    private void ensureCreatedAt(UserDocument userDocument) {
        if (userDocument.getCreatedAt() == null) {
            userDocument.setCreatedAt(LocalDateTime.now());
        }
    }
}
