package com.travel.travelplan.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "todos") // 데이터베이스의 테이블 이름
public class Todo {

    @Id // 기본 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private int id;

    @ManyToOne //외래키
    @JoinColumn(name = "user_id") 
    private User user;

    private String username;

    private String eventName; // 일정명
    private int category;  // 카테고리 (1: 개인, 2: 친구, 3: 가족, 4: 회사, 5: 공유된 일정)


    @Size(min = 5, message = "Enter at least 5 characters")
    // @NotEmpty
    private String description; // 내용

    private LocalDate targetDateFr; // 기간 시작일
    private LocalDate targetDateTo; // 기간 종료일

    private boolean done; // 완료 여부
    private String remark; // 비고

    

    // 기본 생성자
    public Todo() {}

    // 매개변수가 있는 생성자
    public Todo(int id, String username, String eventName, int category, String description, 
                LocalDate targetDateFr, LocalDate targetDateTo, boolean done, String remark) {
        this.id = id;
        this.username = username;
        this.eventName = eventName; // 일정명
        this.category = category;   // 카테고리
        this.description = description;
        this.targetDateFr = targetDateFr;
        this.targetDateTo = targetDateTo;
        this.done = done;
        this.remark = remark;

    }

    // Getter 및 Setter 메서드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDateFr() {
        return targetDateFr;
    }

    public void setTargetDateFr(LocalDate targetDateFr) {
        this.targetDateFr = targetDateFr;
    }

    public LocalDate getTargetDateTo() {
        return targetDateTo;
    }

    public void setTargetDateTo(LocalDate targetDateTo) {
        this.targetDateTo = targetDateTo;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", username=" + username + ", eventName=" + eventName + 
               ", category=" + category + ", description=" + description + 
               ", targetDateFr=" + targetDateFr + ", targetDateTo=" + targetDateTo + 
               ", done=" + done + ", remark=" + remark + "]";
    }
}
