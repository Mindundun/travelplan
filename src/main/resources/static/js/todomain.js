function togglePriority(button) {
    button.classList.toggle("active"); // 버튼 클릭 시 'active' 클래스 토글
    const pin = button.nextElementSibling; // 버튼 옆의 압정 요소
    pin.style.display = button.classList.contains("active") ? "block" : "none"; // 활성화되면 압정 표시
}
