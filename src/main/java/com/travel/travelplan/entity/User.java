package com.travel.travelplan.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "user",
    indexes = {
        @Index(name = "idx_user_username", columnList = "username")
    })
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username; // email 임

    private String password;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    @Column(nullable = false)
    private String role;

    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "is_verify")
    private Boolean isVerify;   // 이메일 인증 여부

    @Column(name = "verify_date")
    private LocalDateTime verifyDate; // 이메일 인증 일시

    @Column(name = "verify_key")
    private String verifyKey;   // 이메일 인증 코드

    @Column(name = "login_yn")
    private Boolean loginYn;    // 로그인 가능 여부

    @Column(name = "login_yn_date")
    private LocalDateTime loginYnDate; // 로그인 가능 여부 변경일

    @Column(name = "login_date")
    private LocalDateTime loginDate; // 로그인 일시

    @Column(name = "is_used")
    private Boolean isUsed; // 탈퇴 여부

    @PrePersist
    public void prepersist() {
        if(this.createDate == null) this.createDate = LocalDateTime.now();
        if(this.isVerify == null) this.isVerify = false;
        if(this.loginYn == null) this.loginYn = false;
        if(this.isUsed == null) this.isUsed = true;
    }

}
