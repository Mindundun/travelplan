package com.travel.travelplan.repository.friend;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer>, FriendRequestRepositoryCustom {

    // 친구 요청 여부 확인
    Boolean existsByRequestUserAndTargetUserAndSentDateBefore(User requestUser, User targetUser, LocalDateTime date);

}
