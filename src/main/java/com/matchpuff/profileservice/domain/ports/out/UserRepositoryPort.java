package com.matchpuff.profileservice.domain.ports.out;

import com.matchpuff.profileservice.domain.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

	User save(StudentProfile student);
	User save(Admin admin);
	User save(Organizer organizer);
	void delete(UUID userId);
	Optional<User> findById(UUID id);
	Optional<User> findByEmail(String email);
	User update(UUID id, User user);
    List<User> findAll();

	List<StudentProfile> findAllStudents();

	List<User> findAllByIds(List<UUID> ids);

}

