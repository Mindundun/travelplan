package com.travel.travelplan.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travel.travelplan.entity.Todo;
import com.travel.travelplan.repository.TodoListRepository;

import jakarta.validation.Valid;

@Service
public class TodoListService {

    
    private static final List<Todo> todos = new ArrayList<>();
    
    private static int todosCount = 0;

    public List<Todo> findByUsername(String username) {
        Predicate<? super Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(username);
        return todos.stream().filter(predicate).toList();
    }
    
    @Autowired
    private TodoListRepository todolistRepository;

    // 로그인한 사용자 기준으로 총 일정 수를 조회
    public long getTotalCount(String username) {
        return todolistRepository.countByUsername(username); // 총 일정 개수
    }

    // 로그인한 사용자 기준으로 완료된 일정 수를 조회
    public long getCompletedCount(String username) {
        return todolistRepository.countByUsernameAndDone(username, true); // 완료된 일정 개수
    }

    // 로그인한 사용자 기준으로 미완료된 일정 수를 조회
    public long getIncompleteCount(String username) {
        return todolistRepository.countByUsernameAndDoneFalse(username); // 미완료된 일정 개수
    }

    public void addTodo(String username, String eventName, int category, String description, LocalDate targetDateFr, LocalDate targetDateTo, boolean done, String remark) {

        // 새로운 Todo 객체 생성
		Todo todo = new Todo(++todosCount, username, eventName, category, description, targetDateFr, targetDateTo, done, remark);

        // Todo 리스트에 추가
        todos.add(todo);
        
        // Todo가 리스트에 추가되었음을 로그로 출력
        System.out.println("Todo added: " + todo);
    }

    public void deleteById(int id) {
        // 삭제할 때는 todo.getId() == id
        Predicate<? super Todo> predicate = todo -> todo.getId() == id;
        todos.removeIf(predicate);
    }

    public Todo findByid(int id) {
        Predicate<? super Todo> predicate = todo -> todo.getId() == id;
        Todo todo = todos.stream().filter(predicate).findFirst().get();
        return todo;
    }

    public void updateTodo(@Valid Todo todo) {
        deleteById(todo.getId());  // 삭제 후
        todos.add(todo);  // 추가하는 방식으로 update 처리
    }
}
