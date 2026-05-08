package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.response.ScheduleResponse;
import com.matchpuff.profileservice.application.dto.response.TagResponse;
import com.matchpuff.profileservice.application.dto.response.UserAuthResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.application.dto.response.UserResponseProfilePhoto;
import com.matchpuff.profileservice.domain.model.Schedule;
import com.matchpuff.profileservice.domain.model.Tag;
import com.matchpuff.profileservice.domain.model.User;

import java.util.List;

public interface UserMapper {

     UserResponse toResponse(User user);

     List<ScheduleResponse> toScheduleResponseList(List<Schedule> schedules);

     List<TagResponse> toTagResponseList(List<Tag> tags);

     String resolveUserType(User user);

     UserResponseProfilePhoto toResponseProfilePhoto(User user);

     UserAuthResponse toAuthResponse(User user);
}
