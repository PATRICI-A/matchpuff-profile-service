package com.matchpuff.profileservice.domain.ports.out;

import com.matchpuff.profileservice.application.dto.event.FriendshipCreatedNotificationDto;

public interface FriendshipEventPublisherPort {

    void publishFriendshipCreated(FriendshipCreatedNotificationDto event);
}
