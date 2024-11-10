package com.travel.travelplan.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.travel.travelplan.dto.CustomUserDetails;
import com.travel.travelplan.entity.User;
import com.travel.travelplan.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;

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

        String redirectUrl = getReturnUrl(request, response);

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("user", user); // 일단 유저 엔티티를 세션에 올린다.
        newSession.setMaxInactiveInterval(60 * 30); // 세션 유지시간을 30분으로 설정한다.

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String getReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        String sessionRedirectUrl = (String) request.getSession().getAttribute("redirectUrl");

        String redirectUrl = (sessionRedirectUrl != null) ? sessionRedirectUrl :
                            (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";
        log.debug("Redirecting to URL: {}", redirectUrl); // 디버깅용 로그 추가
        return redirectUrl;
    }

}