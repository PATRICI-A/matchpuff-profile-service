package com.matchpuff.profileservice.entrypoints.rest.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
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
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPasswordHash(request.getPassword());
        student.setGender(request.getGender());
        student.setCareer(request.getCarreer());
        student.setSemester(request.getSemester() == null ? 0 : request.getSemester());
        student.setStudentCarnet(request.getStudentCarnet());
        student.setPhotoUrl(request.getPhotourl());
        student.setBiography(request.getBiography());
        student.setPrivacyLevel(request.getPrivacyLevel());
        student.setTags(toDomainTags(request.getTags()));
        student.setSchedules(toDomainSchedules(request.getSchedules()));
        student.setDateOfBirth(request.getDateOfBirth());
        return student;
    }

    public static Admin toDomain(UserAdminRequest request) {
        if (request == null) {
            return null;
        }

        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPasswordHash(request.getPassword());
        admin.setGender(request.getGender());

        return admin;
    }

    public static Organizer toDomain(UserOrganizerRequest request) {
        if (request == null) {
            return null;
        }

        Organizer organizer = new Organizer();
        organizer.setName(request.getName());
        organizer.setEmail(request.getEmail());
        organizer.setPasswordHash(request.getPassword());
        organizer.setGender(request.getGender());
        organizer.setContactInfo(request.getContactInfo());

        return organizer;
    }

    private static List<Tag> toDomainTags(List<TagRequest> tags) {
        if (tags == null) {
            return Collections.emptyList();
        }

        return tags.stream().map(tagRequest -> {
            Tag tag = new Tag();
            tag.setName(tagRequest.getName());
            tag.setCategory(tagRequest.getCategory());
            return tag;
        }).toList();
    }

    private static List<Schedule> toDomainSchedules(List<ScheduleRequest> schedules) {
        if (schedules == null) {
            return Collections.emptyList();
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

        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setCategory(request.getCategory());
        return tag;
    }
}
