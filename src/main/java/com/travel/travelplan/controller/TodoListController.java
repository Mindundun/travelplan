package com.travel.travelplan.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult; // 추가
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.travel.travelplan.entity.Todo;
import com.travel.travelplan.repository.TodoListRepository;
import com.travel.travelplan.service.TodoListService;

import jakarta.validation.Valid;

@Controller // @Controller 애너테이션 추가
@SessionAttributes("name")
public class TodoListController {

	@Autowired
    private TodoListService todolistService;

	// 내부 enum 정의(카테고리.)
    public enum Category {
        PERSONAL(1, "개인"),
        FRIEND(2, "친구"),
        FAMILY(3, "가족"),
        WORK(4, "회사"),
        SHARED(5, "공유된 일정");

        private final int number;
        private final String name;

        Category(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public static Category fromNumber(int number) {
            for (Category category : Category.values()) {
                if (category.getNumber() == number) {
                    return category;
                }
            }
            throw new IllegalArgumentException("Invalid category number: " + number);
        }
    }

    public TodoListController(TodoListRepository todolistRepository) {
		super();
		this.todolistRepository = todolistRepository; //이렇게 하면 자동적으로 결합(생성자)
	}
		
	//DB 테이블과 소통하기 위한 선언. -> 생성자 인수로 추가
	private TodoListRepository todolistRepository;

	// /todolist
	@RequestMapping("todolist")
	public String listAllTodos(
		ModelMap model,
		@RequestParam(required = false) Integer year,
		@RequestParam(required = false) String category,
		@RequestParam(required = false) String status,
		@RequestParam(required = false) String eventName) {

		String loggedInUsername = getLoggedInUsername(model);
		List<Todo> todos = todolistRepository.findByUsername(loggedInUsername);

		// 년도 필터링
		if (year != null) {
			todos = todos.stream()
						.filter(todo -> (todo.getTargetDateFr().getYear() == year || todo.getTargetDateTo().getYear() == year))
						.collect(Collectors.toList());
		}

		// 카테고리 필터링
		if (category != null && !category.isEmpty()) {
			todos = todos.stream()
						.filter(todo -> Category.fromNumber(todo.getCategory()).getName().equals(category))
						.collect(Collectors.toList());
		}

		// 완료 여부 필터링
		if (status != null && !status.isEmpty()) {
			boolean isCompleted = status.equals("완료");
			todos = todos.stream()
						.filter(todo -> todo.isDone() == isCompleted)
						.collect(Collectors.toList());
		}

		// 일정명 필터링
		if (eventName != null && !eventName.isEmpty()) {
			todos = todos.stream()
						.filter(todo -> todo.getEventName().contains(eventName))
						.collect(Collectors.toList());
		}

		// 모델에 필터링된 일정 정보와 통계 추가
		model.addAttribute("todos", todos);
		model.addAttribute("totalCount", todos.size());
		model.addAttribute("completedCount", todos.stream().filter(Todo::isDone).count());
		model.addAttribute("incompleteCount", todos.stream().filter(todo -> !todo.isDone()).count());
		model.addAttribute("year", year);  // 사용자가 선택한 년도를 모델에 추가
		model.addAttribute("category", category);  // 사용자가 선택한 카테고리 추가
		model.addAttribute("status", status);  // 사용자가 선택한 완료 여부 추가
		model.addAttribute("eventName", eventName);  // 사용자가 입력한 일정명 추가

		return "todolist";
	}

    //GET
	@RequestMapping(value="add-todo", method=RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		String username = getLoggedInUsername(model);
        Todo todo = new Todo(0, username, "", 1, "", LocalDate.now(), LocalDate.now(), false, "");
    
		model.put("todo", todo);
		return "todo";
	}
	
	//POST
	//@RequestParam String description을 사용하지 않고 Todo의 Description을 가져오도록
	@RequestMapping(value="add-todo", method=RequestMethod.POST)
	public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			return "todo";
		}
		
		String username = getLoggedInUsername(model);
		todo.setUsername(username);//Todo Bean 넣기
		todolistRepository.save(todo);//왜냐 todoRepository는 모든 속성을 받는 메서드가 없기 때문
		
		//todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), todo.isDone());
		
		return "redirect:todolist";
	}
	
	@RequestMapping("delete-todo")
	public String deleteTodo(@RequestParam int id) {
        System.out.println("삭제 요청 받은 ID: " + id);  // 로그 출력
		todolistRepository.deleteById(id);
		//todoService.deleteById(id);
		return "redirect:todolist";
	}
	
	@RequestMapping(value="update-todo", method=RequestMethod.GET)
	public String showUpdateTodo(@RequestParam int id,ModelMap model) {
		Todo todo = todolistRepository.findById(id).get();
		model.addAttribute("todo", todo);//model에 todo추가
		return "todo";
	}
	
	@RequestMapping(value="update-todo", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> updateTodo(ModelMap model, @RequestBody @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			List<String> list = result.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(list);
		}
		
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todolistRepository.save(todo);
		//todoService.updateTodo(todo);
		
		return ResponseEntity.ok().build();
	}

    private String getLoggedInUsername(ModelMap model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
	}

    @RequestMapping(value = "/todomain", method = RequestMethod.GET)
    public String todomain() {
        return "todomain";
    }
}
