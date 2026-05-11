package com.matchpuff.profileservice.entrypoints.rest.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.TagRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "photoUrl", source = "photourl")
    @Mapping(target = "schedules", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "tags", expression = "java(new java.util.ArrayList<>())")
    StudentProfile toDomain(UserStudentRequest request);

    @Mapping(target = "passwordHash", source = "password")
    Admin toDomain(UserAdminRequest request);

    Admin toDomain(UserAdminUpdateRequest request);

    @Mapping(target = "passwordHash", source = "password")
    Organizer toDomain(UserOrganizerRequest request);

    Organizer toDomain(UserOrganizerUpdateRequest request);

    @Mapping(target = "schedules", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    StudentProfile toDomain(UserStudentUpdateRequest request);

    default Schedule toDomain(ScheduleRequest request) {
        if (request == null) return null;
        return new Schedule(
                request.getDayOfWeek(),
                request.getName(),
                request.getStartTime(),
                request.getEndTime()
        );
    }

    default Tag toDomain(TagRequest request) {
        if (request == null) return null;
        return new Tag(request.getName(), request.getCategory());
    }
}
