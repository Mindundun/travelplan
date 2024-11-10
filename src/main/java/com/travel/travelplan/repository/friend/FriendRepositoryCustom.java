package com.travel.travelplan.repository.friend;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.travel.travelplan.entity.User;

public interface FriendRepositoryCustom {

    List<User> findFriendListByUser(User user, Pageable pageable, String search);

}
