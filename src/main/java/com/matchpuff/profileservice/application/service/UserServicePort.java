package com.matchpuff.profileservice.application.service;

import java.util.List;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;

public interface UserServicePort {
	UserResponse createStudentUser(StudentProfile user);
    UserResponse createAdminUser(Admin user);
    UserResponse createOrganizerUser(Organizer user);
    void deleteUser(String userId);
	UserResponse getUser(String userId);
	UserResponse updateUser(String userId, StudentProfile user);
    UserResponse addSchedule(String userId, Schedule schedule);
    UserResponse addTag(String userId, Tag tag);
    UserResponseProfilePhoto updateProfileImage(String userId, byte[] file, String contentType);


    List<UserResponse> getAllUsers();
    List<UserResponse> getAllStudentProfiles();

}

