package com.travel.travelplan.repository.friend;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.FriendRequest;

public interface FriendRequestRepositoryCustom {

    // 친구 요청 목록 보기
    List<FriendRequest> findFriendRequestListByUser(User user, Pageable pageable, String search);

    Integer countFriendRequestListByUser(User user, String search);

    // 친구 요청 받은 목록 보기
    List<FriendRequest> findFriendRequestReceivedListByUser(User user, Pageable pageable, String search);

    Integer countFriendRequestReceivedListByUser(User user, String search);

}
