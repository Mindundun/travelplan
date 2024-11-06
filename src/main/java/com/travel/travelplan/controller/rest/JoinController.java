package com.travel.travelplan.controller.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.travel.travelplan.requests.JoinRequest;
import com.travel.travelplan.service.JoinService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@ControllerAdvice(basePackageClasses = JoinController.class)
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<Boolean> joinProcess(@RequestBody @Valid JoinRequest joinRequest) {
        log.debug("회원가입 프로세스 시작 joinRequest: {}", joinRequest);
        joinService.joinProcess(joinRequest);
        log.debug("회원가입 프로세스 종료");
        return ResponseEntity.ok(true);
    }

    @PostMapping("/send/email")
    public ResponseEntity<Boolean> sendRegisterMail(@RequestBody Map<String, String> map) {
        String email = map.getOrDefault("email", "");
        log.debug("회원가입 이메일 전송 시작 email: {}", email);
        joinService.sendRegisterMail(email);
        log.debug("회원가입 이메일 전송 종료");
        return ResponseEntity.ok(true);
    }

    // 이메일 인증 확인
    @PostMapping("/verify/email")
    public ResponseEntity<Boolean> verifyEmail(@RequestBody Map<String,String> map) {

        String email = map.getOrDefault("email", "");
        String key = map.getOrDefault("key", "");

        log.debug("이메일 인증 시작 email: {}, key: {}", email, key);

        joinService.verifyEmail(email, key);

        log.debug("이메일 인증 종료");

        return ResponseEntity.ok(true);
    }

    @GetMapping("/verify/nickName")
    @ResponseBody
    public ResponseEntity<Boolean> getMethodName(@RequestParam("nickName") String nickName) {
        log.debug("닉네임 중복 검사 시작 nickName: {}", nickName);
        Boolean result = joinService.nickNameCheck(nickName);
        log.debug("닉네임 중복 검사 종료 result: {}", result);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // return ResponseEntity.internalServerError().body("문제발생..");
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

}
