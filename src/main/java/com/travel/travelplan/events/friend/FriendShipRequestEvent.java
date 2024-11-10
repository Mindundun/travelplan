package com.travel.travelplan.events.friend;

import org.springframework.context.ApplicationEvent;

import com.travel.travelplan.entity.friend.FriendRequest;

public class FriendShipRequestEvent extends ApplicationEvent {

    private final FriendRequest friendRequest;

    public FriendShipRequestEvent(FriendRequest friendRequest) {
        super(friendRequest);
        this.friendRequest = friendRequest;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

}
