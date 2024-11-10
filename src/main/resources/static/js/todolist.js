
// 모달 열기 함수
function openModal() {
    document.getElementById("modal").style.display = "block";
}

// 모달에 내용 삽입 함수
function settingContent(obj) {
    const row = obj.closest("td").closest("tr");
    // 행의 각 데이터 가져오기
    const itemId = row.dataset.id;
    const eventName = row.querySelector(".eventName").textContent;
    const category = row.querySelector(".category").textContent;
    const targetDateFrom = row.querySelector(".targetDateFrom").textContent;
    const targetDateTo = row.querySelector(".targetDateTo").textContent;
    // const done = row.querySelector(".done").textContent;
    const remark = row.querySelector(".remark").textContent;

    // 모달의 입력 필드에 값 설정
    document.getElementById("itemId").value = itemId;
    document.getElementById("title").value = eventName;
    document.getElementById("category").value = category;
    document.getElementById("start-date").value = targetDateFrom;
    document.getElementById("end-date").value = targetDateTo;
    // document.getElementById("modal-done").value = done;
    document.getElementById("details").value = remark;

}

function openModalAndSettingContent(obj) {
    settingContent(obj);
    openModal();
}

// 저장 버튼 클릭 시 동작
function saveTodo() {
    // 저장 로직 추가
    // 모달의 입력 필드에 값 설정
    const id = document.getElementById("itemId").value;
    const eventName = document.getElementById("title").value;
    const category = document.getElementById("category").value;
    const targetDateFrom = document.getElementById("start-date").value;
    const targetDateTo = document.getElementById("end-date").value;
    // const done = document.getElementById("modal-done").value;
    const remark = document.getElementById("details").value;

    const formData = {
        id : id,
        eventName : eventName,
        category : category,
        targetDateFrom : targetDateFrom,
        targetDateTo : targetDateTo,
        // done : done,
        remark : remark
    }

    debugger;
    fetch('/update-todo', {
        method : 'POST',
        headers: {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify(formData)
    })
    .then(res => {
        if(res.ok){
            alert('업뎃성공');
            window.location.reload();
        } else {
            throw Exception();
        }
    })
    .catch(e => {
        console.error(e);
    })

    closeModal();
}

// 모달 닫기 함수
function closeModal() {
    document.getElementById("modal").style.display = "none";
}


document.addEventListener("DOMContentLoaded", function() {
// 수정 버튼 클릭 시 모달 열기
// document.querySelectorAll('.btn-success').forEach(button => {
//     button.addEventListener('click', openModalAndSettingContent(button));
// });



// 저장 버튼 클릭시 이벤트
function saveBtnClickEvent() {

}

// 모달 바깥 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById("modal");
    if (event.target === modal) {
        closeModal();
    }
}

// 이메일 목록을 저장할 배열
let emailList = [];

// 이메일 추가 함수
function addEmail(event) {
    if (event.key === "Enter") {
        event.preventDefault();
        const emailInput = document.getElementById("email-input");
        const email = emailInput.value.trim();

        // 이메일 유효성 검사 (간단한 패턴)
        if (validateEmail(email)) {
            if (!emailList.includes(email)) {
                emailList.push(email);
                displayEmails();
                emailInput.value = ""; // 입력 필드 초기화
            } else {
                alert("이미 추가된 이메일입니다.");
            }
        } else {
            alert("유효한 이메일 주소를 입력하세요.");
        }
    }
}

// 이메일 표시 함수
function displayEmails() {
    const emailListContainer = document.getElementById("email-list");
    emailListContainer.innerHTML = ""; // 기존 이메일 목록 초기화

    emailList.forEach(email => {
        const emailTag = document.createElement("span");
        emailTag.classList.add("email-tag");
        emailTag.textContent = email;

        const removeButton = document.createElement("span");
        removeButton.textContent = "x";
        removeButton.classList.add("remove-button");
        removeButton.onclick = () => removeEmail(email);

        emailTag.appendChild(removeButton);
        emailListContainer.appendChild(emailTag);
    });
}

// 이메일 삭제 함수
function removeEmail(email) {
    emailList = emailList.filter(item => item !== email);
    displayEmails();
}

// 이메일 유효성 검사 함수
function validateEmail(email) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}

});

// 댓글 추가 함수
function addComment() {
    const commentInput = document.getElementById('comment');
    const commentText = commentInput.value.trim();
    
    if (commentText) {
        const commentList = document.getElementById('comment-list');
        const newComment = document.createElement('div');
        newComment.classList.add('comment');
        newComment.textContent = commentText;
        
        // 댓글 목록에 추가
        commentList.appendChild(newComment);
        
        // 댓글 입력란 비우기
        commentInput.value = '';
    }
}

// 댓글 입력 시, Enter 키를 눌러서 댓글 추가
document.getElementById('comment').addEventListener('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        addComment();
    }
});



// 날짜별 할 일 입력 테이블 생성 함수
function generateTaskTable() {
    const startDateInput = document.getElementById('start-date').value;
    const endDateInput = document.getElementById('end-date').value;
    const tableBody = document.getElementById('task-table-body');

    // 테이블 내용 초기화
    tableBody.innerHTML = '';

    // 날짜 형식이 올바른지 확인
    if (!startDateInput || !endDateInput) {
        alert("시작일과 종료일을 모두 입력하세요.");
        return;
    }

    const startDate = new Date(startDateInput);
    const endDate = new Date(endDateInput);

    // 시작일이 종료일보다 이후일 경우 오류 메시지
    if (startDate > endDate) {
        alert("시작일이 종료일보다 이후일 수 없습니다.");
        return;
    }

    // 날짜를 하나씩 증가시키며 반복
    for (let current = new Date(startDate); current <= endDate; current.setDate(current.getDate() + 1)) {
        const row = document.createElement('tr');

        // 날짜 셀 생성
        const dateCell = document.createElement('td');
        dateCell.textContent = current.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 표시
        row.appendChild(dateCell);

        // 할 일 입력란 셀 생성
        const taskCell = document.createElement('td');
        const taskInput = document.createElement('input');
        taskInput.type = 'text';
        taskInput.placeholder = '할 일을 입력하세요';
        taskInput.classList.add("task-input");
        taskCell.appendChild(taskInput);
        row.appendChild(taskCell);

        // 테이블에 행 추가
        tableBody.appendChild(row);
    }
}

// 시작일자 또는 종료일자가 변경될 때마다 테이블을 업데이트
document.getElementById('start-date').addEventListener('change', generateTaskTable);
document.getElementById('end-date').addEventListener('change', generateTaskTable);


