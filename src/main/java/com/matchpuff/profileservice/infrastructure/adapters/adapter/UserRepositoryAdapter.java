package com.matchpuff.profileservice.infrastructure.adapters.adapter;

import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.exceptions.InvalidInputException;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.ports.out.UserRepositoryPort;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
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
    public Optional<User> findById(String id) {
        return userRepository.findById(id)
                .flatMap(this::convertDocumentToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .flatMap(this::convertDocumentToDomain);
    }

    @Override
    public User update(String id, StudentProfile user) {
        user.setId(id);
        StudentProfileDocument doc = UserMapper.toDocument(user);
        StudentProfileDocument updated = userRepository.save(doc);
        return UserMapper.toDomain(updated);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .flatMap(doc -> convertDocumentToDomain(doc).stream())
                .toList();
    }

    // ── Helper methods for type discrimination ──────────────────────────────────

    /**
     * Convierte un UserDocument al modelo de dominio correspondiente según su tipo.
     * Maneja los tres tipos de usuarios: STUDENT, ADMIN, ORGANIZER.
     */
    private Optional<User> convertDocumentToDomain(UserDocument doc) {
        if (doc == null || doc.getUserType() == null) {
            return Optional.empty();
        }

        return switch (doc.getUserType()) {
            case STUDENT -> convertStudentDocument((StudentProfileDocument) doc);
            case ADMIN -> convertAdminDocument((AdminProfileDocument) doc);
            case ORGANIZER -> convertOrganizerDocument((OrganizerProfileDocument) doc);
        };
    }

    /**
     * Convierte un StudentProfileDocument (tipo STUDENT) al modelo de dominio StudentProfile.
     */
    private Optional<User> convertStudentDocument(StudentProfileDocument doc) {
        try {
            if (doc instanceof StudentProfileDocument) {
                StudentProfile profile = UserMapper.toDomain(doc);
                return Optional.of((User) profile);
            }
        } catch (Exception e) {
            // Log error si es necesario
        }
        return Optional.empty();
    }

    /**
     * Convierte un AdminProfileDocument (tipo ADMIN) al modelo de dominio Admin.
     * Actualmente Admin no tiene campos específicos adicionales.
     */
    private Optional<User> convertAdminDocument(AdminProfileDocument doc) {
        try {
            Admin admin = new Admin();
            admin.setId(doc.getId());
            admin.setName(doc.getName());
            admin.setEmail(doc.getEmail());
            admin.setGender(doc.getGender());
            admin.setDateOfBirth(doc.getBirthdate() == null ? null : doc.getBirthdate().toLocalDate());
            admin.setCreatedAt(doc.getCreatedAt());
            return Optional.of((User) admin);
        } catch (Exception e) {
            // Log error si es necesario
        }
        return Optional.empty();
    }

    /**
     * Convierte un OrganizerProfileDocument (tipo ORGANIZER) al modelo de dominio Organizer.
     * Mapea el campo 'contact' del documento al campo 'contactInfo' del modelo.
     */
    private Optional<User> convertOrganizerDocument(OrganizerProfileDocument doc) {
        try {
            Organizer organizer = new Organizer();
            organizer.setId(doc.getId());
            organizer.setName(doc.getName());
            organizer.setEmail(doc.getEmail());
            organizer.setGender(doc.getGender());
            organizer.setDateOfBirth(doc.getBirthdate() == null ? null : doc.getBirthdate().toLocalDate());
            organizer.setCreatedAt(doc.getCreatedAt());
            organizer.setContactInfo(doc.getContact());  // Mapear 'contact' → 'contactInfo'
            return Optional.of((User) organizer);
        } catch (Exception e) {
            // Log error si es necesario
        }
        return Optional.empty();
    }


    private boolean mailExistsForException(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidInputException("El correo ya está registrado");
        }
        return false;
    }
}
