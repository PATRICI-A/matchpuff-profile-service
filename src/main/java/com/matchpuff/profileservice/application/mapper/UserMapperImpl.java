package com.matchpuff.profileservice.application.mapper;

import java.util.Collections;
import java.util.List;

import com.matchpuff.profileservice.application.dto.response.AdminResponse;
import com.matchpuff.profileservice.application.dto.response.OrganizerResponse;
import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.StudentProfileResponse;
import com.matchpuff.profileservice.application.dto.response.TagResponse;
import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.User;
import com.matchpuff.profileservice.domain.model.Tag;
import org.springframework.stereotype.Component;


@Component
public class UserMapperImpl implements UserMapper {
    

    @Override
    public UserResponse toResponse(User user) {
        if (user == null) {
               return null;
          }

          if (user instanceof StudentProfile student) {
               String dateOfBirthStr = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null;
               String genderStr = student.getGender() != null ? student.getGender().toString() : null;
               return StudentProfileResponse.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .email(student.getEmail())
                    .createdAt(student.getCreatedAt())
                    .userType(resolveUserType(student))
                    .gender(genderStr)
                    .dateOfBirth(dateOfBirthStr)
                    .career(student.getCareer())
                    .semester(student.getSemester())
                    .studentCarnet(student.getStudentCarnet())
                    .photoUrl(student.getPhotoUrl())
                    .biography(student.getBiography())
                    .privacyLevel(student.getPrivacyLevel())
                    .schedules(toScheduleResponseList(student.getSchedules()))
                    .tags(toTagResponseList(student.getTags()))
                    .build();
          }

          if (user instanceof Organizer organizer) {
               return OrganizerResponse.builder()
                    .id(organizer.getId())
                    .name(organizer.getName())
                    .email(organizer.getEmail())
                    .createdAt(organizer.getCreatedAt())
                    .gender(organizer.getGender() != null ? organizer.getGender().toString() : null)
                    .userType(resolveUserType(organizer))
                    .contactInfo(organizer.getContactInfo())
                    .build();
          }

          if (user instanceof Admin admin) {
               return AdminResponse.builder()
                    .id(admin.getId())
                    .name(admin.getName())
                    .email(admin.getEmail())
                    .createdAt(admin.getCreatedAt())
                    .gender(admin.getGender() != null ? admin.getGender().toString() : null)
                    .userType(resolveUserType(admin))
                    .build();
          }

          return UserResponse.builder()
               .id(user.getId())
               .name(user.getName())
               .email(user.getEmail())
               .createdAt(user.getCreatedAt())
               .gender(user.getGender() != null ? user.getGender().toString() : null)
               .userType(resolveUserType(user))
               .build();
    }

    @Override
    public List<ScheduleResponse> toScheduleResponseList(List<Schedule> schedules) {
        if (schedules == null) {
          return Collections.emptyList();
          }

          return schedules.stream().map(schedule -> {
               ScheduleResponse response = new ScheduleResponse();
               response.setDayOfWeek(schedule.getDayOfWeek());
               response.setName(schedule.getName());
               response.setStartTime(schedule.getStartTime());
               response.setEndTime(schedule.getEndTime());
               return response;
          }).toList();
    }

    @Override
    public List<TagResponse> toTagResponseList(List<Tag> tags) {
        if (tags == null) {
               return Collections.emptyList();
          }

          return tags.stream().map(tag -> {
               TagResponse response = new TagResponse();
               response.setName(tag.getName());
               response.setCategory(tag.getCategory());
               return response;
          }).toList();
    }

    @Override
    public String resolveUserType(User user) {
        if (user instanceof StudentProfile) {
               return "STUDENT";
          }
          if (user instanceof Admin) {
               return "ADMIN";
          }
          if (user instanceof Organizer) {
               return "ORGANIZER";
          }
          return null;
    }

    @Override
    public UserResponseProfilePhoto toResponseProfilePhoto(User user) {
        if (!(user instanceof StudentProfile student)) {
               return null;
          }

          return UserResponseProfilePhoto.builder()
                  .id(student.getId())
                  .name(student.getName())
                  .email(student.getEmail())
                  .profileImageUrl(student.getPhotoUrl())
                  .build();
     }
    
     @Override
     public UserAuthResponse toAuthResponse(User user) {
          if (user == null) {
               return null;
          }

          return UserAuthResponse.builder()
               .id(user.getId())
               .email(user.getEmail())
               .userType(resolveUserType(user))
               .verified(user.isVerified())
               .passwordHash(user.getPasswordHash())
               .build();
     }
}
