package com.matchpuff.profileservice.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagInfo {
    private UUID id;
    private String name;
    private UUID categoryId;
}
