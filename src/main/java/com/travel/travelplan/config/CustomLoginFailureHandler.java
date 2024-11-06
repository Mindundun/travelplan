package com.travel.travelplan.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        
        String username = request.getParameter("username");  // formData로 전달된 사용자명
        String password = request.getParameter("password");  // formData로 전달된 비밀번호
        log.debug("로그인 실패: username={}, password={}", username, password);

        // 실패 시, 응답을 JSON 형태로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 실패 원인에 따라 적절한 메시지를 설정
        String errorMessage = "로그인 실패";
        if (exception != null || exception instanceof BadCredentialsException) {
            log.debug("로그인 실패: {}", exception.getMessage());
            errorMessage = "잘못된 사용자명 또는 비밀번호";
        }

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // 실패 응답을 Map 형태로 준비
            Map<String, String> responseData = Map.of(
                "error", errorMessage
            );

            // 응답을 JSON 형식으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String responseDataStr = objectMapper.writeValueAsString(responseData);

            // 응답에 JSON 데이터 작성
            outputStream.write(responseDataStr.getBytes("UTF-8"));
            outputStream.flush();
        }
    }

}
