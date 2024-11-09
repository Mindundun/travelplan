// 기본 설정
let currentYear = 2024;

// 연도를 변경하는 함수
function changeYear(offset) {
    currentYear += offset;
    document.getElementById("current-year").innerText = currentYear; // 현재 연도 텍스트 업데이트
    updateTodoList(); // 해당 연도의 일정을 업데이트
}

// 일정 데이터를 관리할 객체 (예시)
const todoData = {
    2023: [
        { month: "1월", tasks: ["민경이 이직하기", "성식이 이직하기"] },
        { month: "3월", tasks: ["뷰 공부하기"] },
    ],
    2024: [
        { month: "1월", tasks: ["민경이 이직하기", "프로젝트 진행하기"] },
        { month: "5월", tasks: ["여행 계획 세우기"] },
    ],
    2025: [
        { month: "2월", tasks: ["새로운 프로젝트 시작"] },
        { month: "4월", tasks: ["뷰 공부하기"] },
    ]
};

// 일정 목록을 업데이트하는 함수
function updateTodoList() {
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML = ''; // 기존 내용을 비웁니다.

    const yearData = todoData[currentYear] || []; // 현재 연도의 데이터 가져오기

    yearData.forEach(monthData => {
        const todoBox = document.createElement("div");
        todoBox.classList.add("todo-box");

        const monthHeader = document.createElement("h3");
        monthHeader.classList.add("sticker");
        monthHeader.innerText = monthData.month;
        todoBox.appendChild(monthHeader);

        const inputBox = document.createElement("div");
        inputBox.classList.add("input-box");

        monthData.tasks.forEach((task, index) => {
            const todoId = `${currentYear}-${monthData.month}-${index}`;  // 고유 ID 생성 (연도+월+인덱스 조합)
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.id = todoId; // 체크박스에 고유 ID 설정

            const label = document.createElement("label");
            label.setAttribute("for", todoId);  // 라벨에 for 속성 설정
            label.innerText = task;

            // 배경색 선택기 생성 (각 항목마다 한 번만 추가)
            if (index === 0) {  // 첫 번째 항목에만 배경색 선택기 추가
                const backgroundPicker = document.createElement('input');
                backgroundPicker.type = 'color';
                backgroundPicker.classList.add('background-picker');
                backgroundPicker.style.display = 'none';
                inputBox.appendChild(backgroundPicker);
            }

            // 테두리 색상 선택기 생성 (각 항목마다 한 번만 추가)
            if (index === 0) {  // 첫 번째 항목에만 테두리 색상 선택기 추가
                const borderPicker = document.createElement('input');
                borderPicker.type = 'color';
                borderPicker.classList.add('border-picker');
                borderPicker.style.display = 'none';
                inputBox.appendChild(borderPicker);
            }

            inputBox.appendChild(checkbox);
            inputBox.appendChild(label);
        });

        todoBox.appendChild(inputBox);
        todoList.appendChild(todoBox);
    });
}

// 페이지 로딩 시 기본 2024년 일정 표시
document.addEventListener("DOMContentLoaded", function() {
    updateTodoList();
    document.querySelectorAll('.background-picker, .border-picker').forEach(picker => {
        picker.style.display = 'none';  // 색상 선택기 숨기기
    });
});

// 편집 모드 활성화 함수
function editMode() {
    document.querySelectorAll('.background-picker, .border-picker').forEach(picker => {
        picker.style.display = picker.style.display === 'none' ? 'block' : 'none';
    });
}

// 배경색 변경 함수
function changeBackgroundColor(picker) {
    const todoBox = picker.closest('.todo-box');  // 가장 가까운 .todo-box 찾기
    todoBox.style.backgroundColor = picker.value;  // 선택된 배경색 적용
}

// 테두리 색상 변경 함수
function changeBorderColor(picker) {
    const todoBox = picker.closest('.todo-box');  // 가장 가까운 .todo-box 찾기
    todoBox.style.borderColor = picker.value;  // 선택된 테두리색 적용
}

// 동적으로 추가된 색상 선택기에도 이벤트 적용하기 위해 이벤트 위임 사용하기
document.addEventListener('change', function(event) {
    if (event.target.classList.contains('background-picker')) {
        changeBackgroundColor(event.target);
    } else if (event.target.classList.contains('border-picker')) {
        changeBorderColor(event.target);
    }
});

// 체크박스와 색상 선택기를 동적으로 추가하는 함수
function addTodoItem(todoText) {
    const todoList = document.getElementById('todo-list');  // todo 리스트 컨테이너
    const todoItem = document.createElement('div');
    todoItem.classList.add('todo-box');
    
    // 고유한 id를 만들어 체크박스와 레이블을 연결
    const todoId = 'chk' + Date.now(); 

    const checkbox = document.createElement('input');
    checkbox.type = "checkbox";
    checkbox.id = todoId;
    
    const label = document.createElement('label');
    label.setAttribute("for", todoId);
    label.innerText = todoText;
    
    // 배경색 선택기 생성
    const backgroundPicker = document.createElement('input');
    backgroundPicker.type = 'color';
    backgroundPicker.classList.add('background-picker');
    backgroundPicker.style.display = 'none';
    
    // 테두리 색상 선택기 생성
    const borderPicker = document.createElement('input');
    borderPicker.type = 'color';
    borderPicker.classList.add('border-picker');
    borderPicker.style.display = 'none';
    
    todoItem.appendChild(checkbox);
    todoItem.appendChild(label);
    todoItem.appendChild(backgroundPicker);
    todoItem.appendChild(borderPicker);
    
    // 새 항목을 todoList에 추가
    todoList.appendChild(todoItem);
}


// 체크박스 동적 추가 함수 (일정 항목 추가)
const todoList = document.getElementById('todo-list'); // todo 리스트 컨테이너

function addTodoItem(todoText) {
    const todoItem = document.createElement('div');
    todoItem.classList.add('todo-box');
    
    // 고유한 id를 만들어 체크박스와 레이블을 연결
    const todoId = 'chk' + Date.now(); 

    todoItem.innerHTML = `
        <input type="checkbox" id="${todoId}">
        <label for="${todoId}">${todoText}</label>
    `;
    
    todoList.appendChild(todoItem);
}

// 체크 상태에 따라 항목 순서를 변경하는 함수
function reorderTodos() {
    const todoList = document.querySelectorAll('.todo-box');
    
    // 체크된 항목들을 찾습니다.
    const checkedItems = [...todoList].filter(item => item.querySelector('input[type="checkbox"]:checked'));
    
    // 체크되지 않은 항목들을 찾습니다.
    const uncheckedItems = [...todoList].filter(item => !item.querySelector('input[type="checkbox"]:checked'));
    
    // 체크된 항목을 아래로, 체크되지 않은 항목을 위로 이동
    checkedItems.forEach(item => {
        item.style.order = 2;  // 체크된 항목은 아래로
    });
    uncheckedItems.forEach(item => {
        item.style.order = 1;  // 체크되지 않은 항목은 위로
    });
}

// 페이지 로드 시 초기 순서 정렬
document.addEventListener('DOMContentLoaded', reorderTodos);

// 각 체크박스를 체크할 때마다 순서를 업데이트
document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
    checkbox.addEventListener('change', reorderTodos);
});
