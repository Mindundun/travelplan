package com.travel.travelplan.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travelplan.dto.CustomUserDetails;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    // 로그인이 성공한 경우 유저를 세션에 올리고 메인페이지 혹은 보던 페이지로 리다이렉트 시키고자한다.
    // 이를 위해 AuthenticationSuccessHandler를 상속받아서 로그인 성공시 처리할 내용을 구현한다.
    // 이 클래스는 로그인 성공시 처리할 내용을 구현한 클래스이다.

    // 로그인 성공시 처리할 내용을 구현한다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 CustomLoginSuccessHandler 실행");
        log.debug("authentication.getName() => {}", authentication.getName());
        log.debug("authentication.getPrincipal() => {}", authentication.getPrincipal());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // 여기에서 유저에 대한 작업을 진행한다.
        user.setLoginDate(LocalDateTime.now());
        userRepository.save(user);

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("user", user); // 일단 유저 엔티티를 세션에 올린다.
        newSession.setMaxInactiveInterval(60 * 30); // 세션 유지시간을 30분으로 설정한다.

        // JSON 응답을 만들어서 클라이언트에게 전달
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 클라이언트로 redirectUrl을 포함한 JSON 응답 보내기
        try(ServletOutputStream outputStream = response.getOutputStream()) {
            Map<String, String> map = Map.of(
                "result" , "SUCCESS",
                "redirectUrl", getReturnUrl(request, response)
            );
            ObjectMapper objectMapper = new ObjectMapper();
            String responseDataStr = objectMapper.writeValueAsString(map);
            outputStream.write(responseDataStr.getBytes("UTF-8"));
            outputStream.flush();
        };
    }

    private String getReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        RequestCache requestCache = new HttpSessionRequestCache();

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest == null) {
            return null;
            // return request.getSession().getServletContext().getContextPath();
        }
        return savedRequest.getRedirectUrl();
    }

}