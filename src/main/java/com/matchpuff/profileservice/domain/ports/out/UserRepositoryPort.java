package com.matchpuff.profileservice.domain.ports.out;

import com.matchpuff.profileservice.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

	User save(StudentProfile student);
	User save(Admin admin);
	User save(Organizer organizer);
	void delete(String userId);
	Optional<User> findById(String id);
	Optional<User> findByEmail(String email);
	User update(String id, User user);
    List<User> findAll();

	List<StudentProfile> findAllStudents();

}

