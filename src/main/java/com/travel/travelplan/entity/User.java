package com.travel.travelplan.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "is_verify")
    private Boolean isVerify;   // 이메일 인증 여부

    @Column(name = "verify_key")
    private String verifyKey;   // 이메일 인증 코드

    @Column(name = "login_yn")
    private Boolean loginYn;    // 로그인 가능 여부

    @PrePersist
    public void prepersist() {
        this.createDate = LocalDateTime.now();
        this.isVerify = false;
        this.loginYn = false;
    }

    @PreUpdate
    public void updateDate() {
        this.updateDate = LocalDateTime.now();
    }

}
