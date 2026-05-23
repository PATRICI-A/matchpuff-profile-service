package com.matchpuff.profileservice.application.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserMatchProfileDto {
    private UUID id;
    private String career;
    private Integer semester;
    private List<String> tags;
    private List<String> schedulesAvailable;
    private boolean active;
}
