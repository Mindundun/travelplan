package com.travel.travelplan.repository.friend;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    // 친구 요청 목록 조회
    List<FriendRequest> findByRequestUser(User requestUser);

    // 친구 요청 받은 목록 조회
    List<FriendRequest> findByTargetUser(User targetUser);

    Boolean existsByRequestUserAndTargetUserAndSentDateBefore(User requestUser, User targetUser, LocalDateTime date);

}
