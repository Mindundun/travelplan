package com.travel.travelplan.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.travel.travelplan.entity.Todo;

import jakarta.validation.Valid;

@Service
public class TodoListService {
    private static final List<Todo> todos = new ArrayList<>();
    
    private static int todosCount = 0;

    public List<Todo> findByUsername(String username) {
        Predicate<? super Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(username);
        return todos.stream().filter(predicate).toList();
    }

    public void addTodo(String username, String description, LocalDate targetDate, boolean done) {
        // 로그 추가: addTodo 메서드 호출 시, 전달된 인자 출력
        System.out.println("Adding new Todo:");
        System.out.println("Username: " + username);
        System.out.println("Description: " + description);
        System.out.println("Target Date: " + targetDate);
        System.out.println("Done Status: " + done);

        // 새로운 Todo 객체 생성
        Todo todo = new Todo(++todosCount, username, description, targetDate, done);
        
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
