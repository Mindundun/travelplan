package com.travel.travelplan.entity.friend;

import java.time.LocalDateTime;

import com.travel.travelplan.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(
    name = "user_friend",
    indexes = {
        @Index(name = "idx_user_friend_user_own_id", columnList = "user_own_id"),
        @Index(name = "idx_user_friend_user_friend_id", columnList = "user_friend_id")
    })
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_own_id")
    private User userOwn;               // 나의 아이디

    @ManyToOne
    @JoinColumn(name = "user_friend_id")
    private User userFriend;            // 친구 아이디

    @Column(name = "friend_nick_name")
    private String friendNickName;      // 친구 닉네임

    @Column(name = "status")
    private Boolean status;              // 친구 상태

    @Column(name = "create_date")
    private LocalDateTime createDate;    // 등록일시

    @Column(name = "update_date")
    private LocalDateTime updateDate;    // 수정일시

    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.status = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
