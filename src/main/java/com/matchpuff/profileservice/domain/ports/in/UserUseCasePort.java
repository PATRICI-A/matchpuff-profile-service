package com.matchpuff.profileservice.domain.ports.in;

import com.matchpuff.profileservice.domain.model.*;

import java.util.List;

import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;

public interface UserUseCasePort {
    User createStudentUser(StudentProfile student);
    User createAdminUser(Admin admin);
    User createOrganizerUser(Organizer organizer);
    void deleteUser(String userId);
    User getUser(String userId);
    User updateStudentUser(String userId, StudentProfile student);
    User addScheduleToStudent(String userId, Schedule schedule);
    User addTagToStudent(String userId, Tag tag);
    User updateProfileImage(String userId, byte[] file, String contentType);
    List<User> getAllUsers();
    List<StudentProfile> getAllStudentProfiles();
}
