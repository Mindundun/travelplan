package com.travel.travelplan.controller.rest.friend;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.travelplan.service.friend.FriendService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
@RestControllerAdvice(basePackageClasses = FriendShipRestController.class)
public class FriendShipRestController {

    private final FriendService friendService;

    // 친구 목록 보기
    @GetMapping
    public ResponseEntity<Void> getFriendList(HttpServletRequest req) {
        log.info("getFriendList");
        // friendService.getFriendList(req);
        return ResponseEntity.ok().build();
    }

    // 친구 요청 목록 보기
    @GetMapping("/request-list")
    public ResponseEntity<Void> getRequestList(HttpServletRequest req) {
        log.info("getRequestList");
        // friendService.getRequestList(req);
        return ResponseEntity.ok().build();
    }

    // 친구 요청
    @PostMapping("/request-friend")
    public ResponseEntity<Void> requstFriend(HttpServletRequest req, @RequestBody Map<String, Integer> map) {
        log.info("requestFriendshipProcess");
        Integer requestId = map.getOrDefault("requsetId", 0);
        friendService.requestFriendshipProcess(req, requestId);
        return ResponseEntity.ok().build();
    }

}