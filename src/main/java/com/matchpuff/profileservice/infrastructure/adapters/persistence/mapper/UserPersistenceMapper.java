package com.matchpuff.profileservice.infrastructure.adapters.persistence.mapper;

import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.AdminProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.OrganizerProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.ScheduleDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.StudentProfileDocument;
import com.matchpuff.profileservice.infrastructure.adapters.persistence.entity.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPersistenceMapper {

    @Mapping(target = "id", expression = "java(profile.getId() != null ? profile.getId() : java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(profile.getCreatedAt() != null ? profile.getCreatedAt() : java.time.LocalDateTime.now())")
    @Mapping(target = "userType", constant = "STUDENT")
    @Mapping(target = "birthdate", expression = "java(profile.getDateOfBirth() == null ? null : profile.getDateOfBirth().atStartOfDay())")
    @Mapping(target = "photourl", source = "photoUrl")
    @Mapping(target = "scheduleAvailability", source = "schedulesAvailability", defaultExpression = "java(new java.util.ArrayList<>())")
    StudentProfileDocument toDocument(StudentProfile profile);

    @Mapping(target = "id", expression = "java(admin.getId() != null ? admin.getId() : java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(admin.getCreatedAt() != null ? admin.getCreatedAt() : java.time.LocalDateTime.now())")
    @Mapping(target = "userType", constant = "ADMIN")
    AdminProfileDocument toDocument(Admin admin);

    @Mapping(target = "id", expression = "java(organizer.getId() != null ? organizer.getId() : java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(organizer.getCreatedAt() != null ? organizer.getCreatedAt() : java.time.LocalDateTime.now())")
    @Mapping(target = "userType", constant = "ORGANIZER")
    @Mapping(target = "contact", source = "contactInfo")
    OrganizerProfileDocument toDocument(Organizer organizer);

    @Mapping(target = "dateOfBirth", expression = "java(doc.getBirthdate() == null ? null : doc.getBirthdate().toLocalDate())")
    @Mapping(target = "photoUrl", source = "photourl")
    @Mapping(target = "schedulesAvailability", source = "scheduleAvailability")
    @Mapping(target = "tagsId", source = "tagsId")
    StudentProfile toDomain(StudentProfileDocument doc);

    Admin toDomain(AdminProfileDocument doc);

    @Mapping(target = "contactInfo", source = "contact")
    Organizer toDomain(OrganizerProfileDocument doc);

    @Mapping(target = "startTime", source = "startHour")
    @Mapping(target = "endTime", source = "finishHour")
    Schedule toSchedule(ScheduleDocument doc);

    @Mapping(target = "startHour", source = "startTime")
    @Mapping(target = "finishHour", source = "endTime")
    ScheduleDocument toScheduleDocument(Schedule schedule);

    default List<StudentProfile> toDomainList(List<StudentProfileDocument> docs) {
        if (docs == null) return Collections.emptyList();
        return docs.stream().map(this::toDomain).toList();
    }

    default List<Schedule> toScheduleList(List<ScheduleDocument> scheduleDocs) {
        if (scheduleDocs == null) return Collections.emptyList();
        return scheduleDocs.stream().map(this::toSchedule).toList();
    }

    default Optional<User> toDomainByType(UserDocument doc) {
        if (doc == null || doc.getUserType() == null) return Optional.empty();
        return switch (doc.getUserType()) {
            case STUDENT -> Optional.of(toDomain((StudentProfileDocument) doc));
            case ADMIN -> Optional.of(toDomain((AdminProfileDocument) doc));
            case ORGANIZER -> Optional.of(toDomain((OrganizerProfileDocument) doc));
        };
    }
}
