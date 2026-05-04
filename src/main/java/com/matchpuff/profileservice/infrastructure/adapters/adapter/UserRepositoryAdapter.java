package com.matchpuff.profileservice.infrastructure.adapters.adapter;

import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserType;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper.UserMapper;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(StudentProfile student) {
        mailExistsForException(student.getEmail());
        StudentProfileDocument doc = UserMapper.toDocument(student);
        StudentProfileDocument saved = userRepository.save(doc);
        return UserMapper.toDomain(saved);
    }

    @Override
    public User save(Admin admin) {
        mailExistsForException(admin.getEmail());
        AdminProfileDocument doc = UserMapper.toDocument(admin);
        AdminProfileDocument saved = userRepository.save(doc);
        return UserMapper.toDomain(saved);
    }

    @Override
    public User save(Organizer organizer) {
        mailExistsForException(organizer.getEmail());
        OrganizerProfileDocument doc = UserMapper.toDocument(organizer);
        OrganizerProfileDocument saved = userRepository.save(doc);
        return UserMapper.toDomain(saved);
    }

    @Override
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id)
                .flatMap(UserMapper::toDomainByType);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(UserMapper::toDomainByType);
    }

    @Override
    public User update(String id, StudentProfile user) {
        if (user.getEmail() != null) {
            Optional<UserDocument> existing = userRepository.findByEmail(user.getEmail());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new InvalidInputException("The email is already in use by another user.");
            }
        }

        user.setId(id);
        StudentProfileDocument doc = UserMapper.toDocument(user);
        StudentProfileDocument updated = userRepository.save(doc);
        return UserMapper.toDomain(updated);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .flatMap(doc -> UserMapper.toDomainByType(doc).stream())
                .toList();
    }
    
    @Override
    public List<StudentProfile> findAllStudents() {
        return userRepository.findByUserType(UserType.STUDENT).stream()
                .flatMap(doc -> UserMapper.toDomainByType(doc).stream())
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
}
