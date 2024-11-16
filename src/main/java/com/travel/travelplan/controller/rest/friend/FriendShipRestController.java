package com.travel.travelplan.controller.rest.friend;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.travelplan.dto.PagingDto;
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
    public ResponseEntity< PagingDto<User> > getFriendList(
        @PageableDefault(size = 100, sort = "nickName", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getFriendList");
        PagingDto<User> result = friendService.findFriendListByUser(pageable, search);
        return ResponseEntity.ok(result);
    }

    // 특정 사용자의 친구 목록 보기
    @GetMapping("/user/{userId}/friend-list")
    public ResponseEntity< PagingDto<User> > getFriendList(@PathVariable("userId") Integer userId,
        @PageableDefault(size = 100, sort = "nickName", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getFriendList");
        PagingDto<User> result = friendService.findFriendListByUser(userId, pageable, search);
        return ResponseEntity.ok(result);
    }

    // 친구 요청 목록 보기
    @GetMapping("/request-list")
    public ResponseEntity< PagingDto<FriendRequest> > getRequestList(
        @PageableDefault(size = 100, sort = "sentDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getRequestList");
        PagingDto<FriendRequest> result = friendService.findFriendRequestListByUser(pageable, search);
        return ResponseEntity.ok(result);
    }

    // 친구 요청 받은 목록 보기
    @GetMapping("/required-list")
    public ResponseEntity< PagingDto<FriendRequest> > getRequestReceivedList(
        @PageableDefault(size = 100, sort = "sentDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String search
    ) {
        log.info("getRequestReceivedList");
        PagingDto<FriendRequest> result = friendService.findFriendRequestReceivedListByUser(pageable, search);
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

    @PutMapping("/{id}/accept-friend")
    public ResponseEntity<Void> acceptRequest(@PathVariable("id") Integer requestId, @RequestParam("token") String token) {
        log.info("acceptFriendshipProcess");
        friendService.acceptFriendshipProcess(requestId, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject-friend")
    public ResponseEntity<Void> rejectRequest(@PathVariable("id") Integer requestId, @RequestParam("token") String token) {
        log.info("rejectFriendshipProcess");
        friendService.rejectFriendshipProcess(requestId, token);
        return ResponseEntity.ok().build();
    }
}
