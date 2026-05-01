package com.matchpuff.profileservice.application.mapper;

import com.matchpuff.profileservice.application.dto.response.UserResponse;
import com.matchpuff.profileservice.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
     UserResponse toResponse(User user);
}
