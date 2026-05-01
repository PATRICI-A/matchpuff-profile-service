package com.matchpuff.profileservice.infrastructure.adapters.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class TagDocument {

    @Field("name")
    private String name;

    @Field("category")
    private String category;
}
