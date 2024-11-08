
document.addEventListener("DOMContentLoaded", function() {
// 수정 버튼 클릭 시 모달 열기
document.querySelectorAll('.btn-success').forEach(button => {
    button.addEventListener('click', openModal);
});

// 모달 열기 함수
function openModal() {
    document.getElementById("modal").style.display = "block";
}

// 모달 닫기 함수
function closeModal() {
    document.getElementById("modal").style.display = "none";
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

// 저장 버튼 클릭 시 동작
function saveTodo() {
    // 이메일 리스트와 다른 정보 저장 로직 작성
    console.log("공유 대상자 이메일 목록:", emailList);
    closeModal();
}
});