package com.matchpuff.profileservice.domain.ports.in;

import com.matchpuff.profileservice.domain.model.User;

import java.util.List;

import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;

public interface UserUseCasePort {
    User createStudentUser(StudentProfile student);
    User getUser(String userId);
    User updateStudentUser(String userId, StudentProfile student);
    User addScheduleToStudent(String userId, Schedule schedule);
    User addTagToStudent(String userId, Tag tag);
    List<User> getAllUsers();
}
