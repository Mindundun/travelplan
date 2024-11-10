package com.travel.travelplan.controller.rest.friend;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.entity.friend.FriendRequest;
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

    // 나의 친구 목록 보기
    @GetMapping
    public ResponseEntity<List<User>> getFriendList(
        @PageableDefault(size = 100, sort = "nickName", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getFriendList");
        List<User> result = friendService.findFriendListByUser(pageable, search);
        return ResponseEntity.ok(result);
    }

    // 특정 사용자의 친구 목록 보기
    @GetMapping("/user/{userId}/friend-list")
    public ResponseEntity<List<User>> getFriendList(@PathVariable("userId") Integer userId,
        @PageableDefault(size = 100, sort = "nickName", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getFriendList");
        List<User> result = friendService.findFriendListByUser(userId, pageable, search);
        return ResponseEntity.ok(result);
    }

    // 친구 요청 목록 보기
    @GetMapping("/request-list")
    public ResponseEntity<List<FriendRequest>> getRequestList(
        @PageableDefault(size = 100, sort = "sentDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getRequestList");
        List<FriendRequest> result = friendService.findFriendRequestListByUser(pageable, search);
        return ResponseEntity.ok(result);
    }

    // 친구 요청 받은 목록 보기
    @GetMapping("/request-received-list")
    public ResponseEntity<List<FriendRequest>> getRequestReceivedList(
        @PageableDefault(size = 100, sort = "sentDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getRequestReceivedList");
        List<FriendRequest> result = friendService.findFriendRequestReceivedListByUser(pageable, search);
        return ResponseEntity.ok(result);
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
