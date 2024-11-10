package com.travel.travelplan.controller.rest.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.travelplan.entity.User;
import com.travel.travelplan.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/test")
@RequiredArgsConstructor
public class UserControllerTest {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getFriendList() {
        log.info("getFriendList");
        return ResponseEntity.ok(userRepository.findAll());
    }
}
