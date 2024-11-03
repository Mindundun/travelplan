package com.travel.travelplan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.travelplan.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

}
