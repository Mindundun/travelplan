
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


