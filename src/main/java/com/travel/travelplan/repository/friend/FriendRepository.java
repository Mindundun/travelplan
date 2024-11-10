package com.travel.travelplan.repository.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer>, FriendRepositoryCustom {

    // 친구 관계 확인
    Boolean existsByUserOwnAndUserFriend(User userOwn, User userFriend);

    // 친구 관계 삭제
    void deleteByUserOwnAndUserFriend(User userOwn, User userFriend);

}
