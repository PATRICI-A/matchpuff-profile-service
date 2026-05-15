package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class FriendRequest {

    @NotNull(message = "The friend ID cannot be null")
    private UUID friendId;

}
