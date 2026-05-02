package com.matchpuff.profileservice.domain.ports.out;

import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.StudentProfile;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {


	User save(StudentProfile student);
	Optional<User> findById(String id);
	Optional<User> findByEmail(String email);
	User update(String id, StudentProfile user);
    List<User> findAll();

	List<User> findAllStudents();

}

