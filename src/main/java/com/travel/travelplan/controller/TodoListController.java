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
	public String listAllTodos(ModelMap model) {
		String username = getLoggedInUsername(model);
		List<Todo> todos = todolistRepository.findByUsername(username);
		model.addAttribute("todos",todos);
		
		//그냥 repository 호출해도 될 것 같은데 컨트롤러-서비스-레포지토리가 국룰이니까.
		long totalCount = todolistService.getTotalCount(username);
        long completedCount = todolistService.getCompletedCount(username);
        long incompleteCount = todolistService.getIncompleteCount(username);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("incompleteCount", incompleteCount);

		return "todolist";
	}

    //GET
	@RequestMapping(value="add-todo", method=RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		String username = getLoggedInUsername(model);
        Todo todo = new Todo(0, username, "", 1, "", LocalDate.now().plusYears(1), LocalDate.now().plusYears(1), false, "");
    
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
