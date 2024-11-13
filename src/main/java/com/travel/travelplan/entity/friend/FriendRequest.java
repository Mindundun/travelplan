package com.travel.travelplan.entity.friend;

import java.time.LocalDateTime;
import java.util.Random;

import com.travel.travelplan.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
    name = "friend_request")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "request_user_id")
    private User requestUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    private String url;
    private String token;

    private Boolean isSent;
    private LocalDateTime sentDate;

    @Column(name = "is_accepted", nullable = true)
    private Boolean isAccepted;
    private LocalDateTime acceptedDate;

    @PrePersist
    public void prePersist() {
        this.isSent = false;
        if(this.token == null) this.token = generateToken();
    }

    public void send() {
        this.isSent = true;
        this.sentDate = LocalDateTime.now();
    }

    public void accept() {
        this.isAccepted = true;
        this.acceptedDate = LocalDateTime.now();
    }

    public void reject() {
        this.isAccepted = false;
        this.acceptedDate = LocalDateTime.now();
    }

    private String generateToken() { // 추후 jwt 토큰으로 변경
        final int LENGTH = 10;
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer token = new StringBuffer(LENGTH);
        Random random = new Random();
        for (int i = 0; i < LENGTH; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }

}
