package com.matchpuff.profileservice.domain.ports.out;

import java.util.UUID;

public interface FriendshipEventPublisherPort {

    void publishFriendshipCreated(UUID userId1, UUID userId2);
}
