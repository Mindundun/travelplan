package com.travel.travelplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.travelplan.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer>, FriendRepositoryCustom {

}
