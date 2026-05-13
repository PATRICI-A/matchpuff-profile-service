package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.request.ScheduleRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminRequest;
import com.matchpuff.profileservice.application.dto.request.UserAdminUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerRequest;
import com.matchpuff.profileservice.application.dto.request.UserOrganizerUpdateRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentRequest;
import com.matchpuff.profileservice.application.dto.request.UserStudentUpdateRequest;
import com.matchpuff.profileservice.application.dto.response.AdminResponse;
import com.matchpuff.profileservice.application.dto.response.OrganizerResponse;
import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;
import com.matchpuff.profileservice.application.dto.response.TagResponse;
import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserMatchProfileResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @SubclassMapping(source = StudentProfile.class, target = StudentProfileResponse.class)
    @SubclassMapping(source = Organizer.class, target = OrganizerResponse.class)
    @SubclassMapping(source = Admin.class, target = AdminResponse.class)
    UserResponse toResponse(User user);

    @Mapping(target = "dateOfBirth", expression = "java(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null)")
    @Mapping(target = "userType", constant = "STUDENT")
    @Mapping(target = "tags", source = "tagsId")
    @Mapping(target = "schedules", source = "schedulesAvailability")
    StudentProfileResponse toStudentResponse(StudentProfile student);

    @Mapping(target = "userType", constant = "ORGANIZER")
    OrganizerResponse toOrganizerResponse(Organizer organizer);

    @Mapping(target = "userType", constant = "ADMIN")
    AdminResponse toAdminResponse(Admin admin);

    ScheduleResponse toScheduleResponse(Schedule schedule);

    List<ScheduleResponse> toScheduleResponseList(List<Schedule> schedules);

    TagResponse toTagResponse(UUID tagId);

    List<TagResponse> toTagResponseList(List<UUID> tagsId);

    @Mapping(target = "tags", source = "tagsId")
    @Mapping(target = "schedules", source = "schedulesAvailability")
    UserMatchProfileResponse toUserMatchProfileResponse(StudentProfile student);

    default UserMatchProfileResponse toUserMatchProfileResponseFromUser(User user) {
        if (!(user instanceof StudentProfile student)) return null;
        return toUserMatchProfileResponse(student);
    }

    @Mapping(target = "profileImageUrl", source = "photoUrl")
    UserResponseProfilePhoto toStudentResponseProfilePhoto(StudentProfile student);

    default UserResponseProfilePhoto toResponseProfilePhoto(User user) {
        if (!(user instanceof StudentProfile student)) return null;
        return toStudentResponseProfilePhoto(student);
    }

    @Mapping(target = "userType", expression = "java(resolveUserType(user))")
    UserAuthResponse toAuthResponse(User user);

    default String resolveUserType(User user) {
        if (user instanceof StudentProfile) return "STUDENT";
        if (user instanceof Admin) return "ADMIN";
        if (user instanceof Organizer) return "ORGANIZER";
        return null;
    }

    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "photoUrl", source = "photourl")
    @Mapping(target = "schedulesAvailability", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "tagsId", expression = "java(new java.util.ArrayList<>())")
    StudentProfile toDomain(UserStudentRequest request);

    @Mapping(target = "passwordHash", source = "password")
    Admin toDomain(UserAdminRequest request);

    Admin toDomain(UserAdminUpdateRequest request);

    @Mapping(target = "passwordHash", source = "password")
    Organizer toDomain(UserOrganizerRequest request);

    Organizer toDomain(UserOrganizerUpdateRequest request);

    @Mapping(target = "schedulesAvailability", ignore = true)
    @Mapping(target = "tagsId", ignore = true)
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

    
}
