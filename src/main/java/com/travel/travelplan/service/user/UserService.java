package com.travel.travelplan.service.user;

import org.springframework.stereotype.Service;

import com.travel.travelplan.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 1. 유저 목록 보기 ( 사용자 끼리 )
    public void getUserList() {
        log.info("getUserList");
        // userRepository.getUserList();
    }

}
