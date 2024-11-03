package com.travel.travelplan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.travelplan.entity.Todo;

@Repository
public interface TodoListRepository extends JpaRepository<Todo, Integer> {
    // username으로 ToDo 리스트를 조회하는 메서드
    List<Todo> findByUsername(String username);
}
