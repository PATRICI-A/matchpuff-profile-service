package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.TagResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseInfo;
import com.matchpuff.profileservice.domain.model.Admin;
import com.matchpuff.profileservice.domain.model.Organizer;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.StudentProfile;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

     @Mapping(target = "userType", expression = "java(resolveUserType(user))")
     UserResponse toResponse(User user);

     default UserResponseInfo toResponseInfo(User user) {
          if (user == null) {
               return null;
          }

          StudentProfile student = (user instanceof StudentProfile) ? (StudentProfile) user : null;

          return UserResponseInfo.builder()
                  .id(user.getId())
                  .name(user.getName())
                  .email(user.getEmail())
                  .createdAt(user.getCreatedAt())
                  .userType(resolveUserType(user))
                  .gender(user.getGender() == null ? null : user.getGender().name())
                  .dateOfBirth(user.getDateOfBirth() == null ? null : user.getDateOfBirth().toString())
                  .biography(student == null ? null : student.getBiography())
                  .schedules(toScheduleResponseList(student == null ? null : student.getSchedules()))
                  .tags(toTagResponseList(student == null ? null : student.getTags()))
                  .build();
     }

     default List<ScheduleResponse> toScheduleResponseList(List<Schedule> schedules) {
          if (schedules == null) {
               return null;
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

     default List<TagResponse> toTagResponseList(List<Tag> tags) {
          if (tags == null) {
               return null;
          }

          return tags.stream().map(tag -> {
               TagResponse response = new TagResponse();
               response.setName(tag.getName());
               response.setCategory(tag.getCategory());
               return response;
          }).toList();
     }

     default String resolveUserType(User user) {
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
}
