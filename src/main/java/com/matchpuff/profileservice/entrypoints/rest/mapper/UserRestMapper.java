package com.matchpuff.profileservice.entrypoints.rest.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserRequest;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.StudentProfile;

import java.util.List;

public class UserRestMapper {
    private UserRestMapper() {
    }

    public static StudentProfile toDomain(UserRequest request) {
        if (request == null) {
            return null;
        }

        StudentProfile student = new StudentProfile();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setGender(request.getGender());
        student.setCareer(request.getCarreer());
        student.setSemester(request.getSemester() == null ? 0 : request.getSemester());
        student.setPhotoUrl(request.getPhoto());
        student.setBiography(request.getBiography());
        student.setPrivacyLevel(request.getPrivacyLevel());
        student.setTags(toDomainTags(request.getTags()));
        student.setSchedules(toDomainSchedules(request.getSchedules()));
        student.setDateOfBirth(request.getDateOfBirth());
        return student;
    }

    private static List<Tag> toDomainTags(List<TagRequest> tags) {
        if (tags == null) {
            return null;
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
            return null;
        }

        return schedules.stream().map(scheduleRequest -> {
            Schedule schedule = new Schedule();
            schedule.setDayOfWeek(scheduleRequest.getDayOfWeek());
            schedule.setName(scheduleRequest.getName());
            schedule.setStartTime(scheduleRequest.getStartTime());
            schedule.setEndTime(scheduleRequest.getEndTime());
            return schedule;
        }).toList();
    }

    public static Schedule toDomain(ScheduleRequest request) {
        if (request == null) {
            return null;
        }

        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setName(request.getName());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        return schedule;
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
