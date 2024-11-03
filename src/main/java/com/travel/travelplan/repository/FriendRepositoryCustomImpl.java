package com.travel.travelplan.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.travel.travelplan.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    public List<User> findFriendListByUserId(Integer userId) {
        return null;
    }
}
