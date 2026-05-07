package com.matchpuff.profileservice.entrypoints.rest.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;

import java.util.Collections;
import java.util.List;

public class UserRestMapper {
    private UserRestMapper() {
    }

    public static StudentProfile toDomain(UserStudentRequest request) {
        if (request == null) {
            return null;
        }

        StudentProfile student = new StudentProfile();
        if (request.getName() != null) student.setName(request.getName());
        if (request.getEmail() != null) student.setEmail(request.getEmail());
        if (request.getPassword() != null) student.setPasswordHash(request.getPassword());
        if (request.getGender() != null) student.setGender(request.getGender());
        if (request.getCareer() != null) student.setCareer(request.getCareer());
        if (request.getSemester() != null) student.setSemester(request.getSemester());
        if (request.getStudentCarnet() != null) student.setStudentCarnet(request.getStudentCarnet());
        if (request.getPhotourl() != null) student.setPhotoUrl(request.getPhotourl());
        if (request.getBiography() != null) student.setBiography(request.getBiography());
        if (request.getPrivacyLevel() != null) student.setPrivacyLevel(request.getPrivacyLevel());

        student.setTags(toDomainTags(Collections.emptyList()));
        student.setSchedules(toDomainSchedules(Collections.emptyList()));

        if (request.getDateOfBirth() != null) student.setDateOfBirth(request.getDateOfBirth());
        return student;
    }

    public static Admin toDomain(UserAdminRequest request) {
        if (request == null) {
            return null;
        }

        Admin admin = new Admin();
        if (request.getName() != null) admin.setName(request.getName());
        if (request.getEmail() != null) admin.setEmail(request.getEmail());
        if (request.getPassword() != null) admin.setPasswordHash(request.getPassword());
        if (request.getGender() != null) admin.setGender(request.getGender());

        return admin;
    }

    public static Admin toDomain(UserAdminUpdateRequest request) {
        if (request == null) {
            return null;
        }

        Admin admin = new Admin();
        if (request.getName() != null) admin.setName(request.getName());
        if (request.getEmail() != null) admin.setEmail(request.getEmail());
        if (request.getGender() != null) admin.setGender(request.getGender());

        return admin;
    }

    public static Organizer toDomain(UserOrganizerRequest request) {
        if (request == null) {
            return null;
        }

        Organizer organizer = new Organizer();
        if (request.getName() != null) organizer.setName(request.getName());
        if (request.getEmail() != null) organizer.setEmail(request.getEmail());
        if (request.getPassword() != null) organizer.setPasswordHash(request.getPassword());
        if (request.getGender() != null) organizer.setGender(request.getGender());
        if (request.getContactInfo() != null) organizer.setContactInfo(request.getContactInfo());

        return organizer;
    }

    public static Organizer toDomain(UserOrganizerUpdateRequest request) {
        if (request == null) {
            return null;
        }

        Organizer organizer = new Organizer();
        if (request.getName() != null) organizer.setName(request.getName());
        if (request.getEmail() != null) organizer.setEmail(request.getEmail());
        if (request.getGender() != null) organizer.setGender(request.getGender());
        if (request.getContactInfo() != null) organizer.setContactInfo(request.getContactInfo());

        return organizer;
    }

    public static StudentProfile toDomain(UserStudentUpdateRequest request) {
        if (request == null) {
            return null;
        }

        StudentProfile student = new StudentProfile();
        if (request.getName() != null) student.setName(request.getName());
        if (request.getEmail() != null) student.setEmail(request.getEmail());
        if (request.getGender() != null) student.setGender(request.getGender());
        if (request.getCareer() != null) student.setCareer(request.getCareer());
        if (request.getSemester() != null && request.getSemester() > 0) student.setSemester(request.getSemester());
        if (request.getStudentCarnet() != null) student.setStudentCarnet(request.getStudentCarnet());
        if (request.getBiography() != null) student.setBiography(request.getBiography());
        if (request.getPrivacyLevel() != null) student.setPrivacyLevel(request.getPrivacyLevel());
        if (request.getDateOfBirth() != null) student.setDateOfBirth(request.getDateOfBirth());
        return student;
    }

    private static List<Tag> toDomainTags(List<TagRequest> tags) {
        if (tags == null) {
            return new java.util.ArrayList<>();
        }

        return tags.stream()
        .map(t -> new Tag(t.getName(), t.getCategory()))
        .toList();
    }

    private static List<Schedule> toDomainSchedules(List<ScheduleRequest> schedules) {
        if (schedules == null) {
            return new java.util.ArrayList<>();
        }

        return schedules.stream()
            .map(scheduleRequest -> new Schedule(
                scheduleRequest.getDayOfWeek(),
                scheduleRequest.getName(),
                scheduleRequest.getStartTime(),
                scheduleRequest.getEndTime()
            ))
            .toList();
    }

    public static Schedule toDomain(ScheduleRequest request) {
        if (request == null) {
            return null;
        }

        return new Schedule(
            request.getDayOfWeek(),
            request.getName(),
            request.getStartTime(),
            request.getEndTime()
        );
    }

    public static Tag toDomain(TagRequest request) {
        if (request == null) {
            return null;
        }

        return new Tag(request.getName(), request.getCategory());
    }
}
