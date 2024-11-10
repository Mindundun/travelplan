package com.travel.travelplan.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.travelplan.components.SmtpComponent;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.repository.UserRepository;
import com.travel.travelplan.requests.JoinRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SmtpComponent smtpComponent;

    // 회원가입 프로세스
    public void joinProcess(JoinRequest joinRequest) {
        // 파라미터
        String username = joinRequest.getUsername();
        String password = joinRequest.getPassword();
        String nickName = joinRequest.getNickName();

        // validation
        // 1. 이메일 인증 체크
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("이메일인증이 완료되지 않았습니다."));

        if(!user.getIsVerify()){
            throw new IllegalArgumentException("이메일인증이 완료되지 않았습니다.");
        }

        // 3. 닉네임 중복 검사
        if(userRepository.existsByNickName(nickName)){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        // 4. 비밀번호 정규식 체크
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[A-Za-z\\d!@#$%^&*()-+=]{9,16}$"; // 특수문자 1개 이상, 숫자 1개 이상, 영문 1개 이상, 9~16자리
        boolean isValid = password.matches(passwordPattern);
        if(!isValid){
            throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자를 포함하여 9~16자리로 입력해주세요.");
        }

        // 5. 유저 저장
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setNickName(nickName);
        user.setLoginYn(true);
        user.setLoginYnDate(LocalDateTime.now());
        userRepository.save(user);
    }

    // 이메일 전송 + 인증키 저장
    public void sendRegisterMail(String email) {

        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"; // TODO 체크필요
        boolean isValid = email.matches(emailPattern);

        if(!isValid){
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }

        // 인증키 생성
        String randomKey = IntStream.range(0, 4)
                .mapToObj(i -> String.valueOf((int) (Math.random() * 10)))
                .collect(Collectors.joining());

        // 인증키 저장
        userRepository.findByUsername(email).ifPresentOrElse( // 개쩐다..
                (u) -> {
                    u.setVerifyKey(randomKey);
                    userRepository.save(u);
                },
                () -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setVerifyKey(randomKey);
                    newUser.setRole("ROLE_USER");
                    userRepository.save(newUser);
                }
        );

        // 이메일 전송
        try {
            smtpComponent.mailSend(email, "TravelPlan 회원가입 인증 메일", "인증번호: " + randomKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("이메일 전송에 실패했습니다.");
        }

    }

    // 이메일 인증하기
    public void verifyEmail(String email, String key) {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if(!user.getVerifyKey().equals(key)){
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        user.setIsVerify(true);
        user.setVerifyDate(LocalDateTime.now());
        userRepository.save(user);
    }

    // 닉네임 중복 체크
    public Boolean nickNameCheck(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

}