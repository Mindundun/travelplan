package com.travel.travelplan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.travelplan.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByNickName(String nickName);

    Optional<User> findByUsername(String username);

}
