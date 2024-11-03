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
	
	public List<Todo> findByUsername(String username){
		Predicate<? super Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(username);
		return todos.stream().filter(predicate).toList();
	}
	
	public void addTodo(String username, String description, LocalDate targetDate, boolean done) {
		Todo todo = new Todo(++todosCount, username, description, targetDate, done);
		todos.add(todo);
	}
	
	public void deleteById(int id) {
		//삭제할 때는 todo.getId() == id
		//Lamda : todo -> todo.getId() == id : 모든 행에 있는 Bean, 즉 Todo에 있는 모든 Bean에 대해 이 조건 실행
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		todos.removeIf(predicate);
	}

	public Todo findByid(int id) {
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		Todo todo = todos.stream().filter(predicate).findFirst().get(); // todos 스트림 생성 후 filter로 predicate를 사용해서 필터링 및 첫번째 가져오기 
		return todo;
	}

	public void updateTodo(@Valid Todo todo) {
		deleteById(todo.getId()); // 삭제 후
		todos.add(todo);	//추가 하는 방식으로 update처리		
	}
}

