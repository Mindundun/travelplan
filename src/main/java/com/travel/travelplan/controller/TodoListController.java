package com.travel.travelplan.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller; // 추가
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.travel.travelplan.entity.Todo;
import com.travel.travelplan.service.TodoListService;

import jakarta.validation.Valid;

@Controller // @Controller 애너테이션 추가
@SessionAttributes("name")
public class TodoListController {

    private final TodoListService todolistService; // final로 변경하여 불변성을 보장

    public TodoListController(TodoListService todolistService) {
        this.todolistService = todolistService;
    }

    // /todolist
    @RequestMapping("todolist") // 요청 URL
    public String showTodoList(ModelMap model) {
        String username = getLoggedInUsername(model); // 로그인한 사용자 이름 가져오기
        List<Todo> todos = todolistService.findByUsername(username); // 사용자에 대한 Todo 리스트 가져오기
        model.addAttribute("todos", todos); // 모델에 todos 추가
        return "todolist"; // todolist.html 파일 반환
    }

    // GET
    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(ModelMap model) {
        String username = getLoggedInUsername(model);
        Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
        model.put("todo", todo);
        return "todo"; // todo.html 파일 반환
    }

    // POST
    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo"; // 오류가 있을 경우 todo.html 파일 반환
        }

        String username = getLoggedInUsername(model);
        todolistService.addTodo(username, todo.getDescription(), todo.getTargetDate(), false);

        return "redirect:todolist"; // 완료 후 todolist로 리다이렉트
    }

    @RequestMapping("delete-todo")
    public String deleteTodo(@RequestParam int id) {
        todolistService.deleteById(id);
        return "redirect:todolist"; // 삭제 후 todolist로 리다이렉트
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String showUpdateTodo(@RequestParam int id, ModelMap model) {
        Todo todo = todolistService.findByid(id);
        model.addAttribute("todo", todo); // 모델에 todo 추가
        return "todo"; // todo.html 파일 반환
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo"; // 오류가 있을 경우 todo.html 파일 반환
        }

        String username = getLoggedInUsername(model);
        todo.setUsername(username);
        todolistService.updateTodo(todo);

        return "redirect:todolist"; // 완료 후 todolist로 리다이렉트
    }

    private String getLoggedInUsername(ModelMap model) {
        return (String) model.get("name"); // 로그인한 사용자 이름 반환
    }

    @RequestMapping(value = "/todomain", method = RequestMethod.GET)
    public String showUpdateTodo() {
        return "todomain";
    }
}
