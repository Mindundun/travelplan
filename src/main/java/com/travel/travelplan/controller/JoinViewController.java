package com.travel.travelplan.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.travel.travelplan.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinViewController {

    @GetMapping("/join")
    public String join() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인된 사용자라면 리디렉션
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails;
        if (isLoggedIn) {
            return "redirect:/";
        }

        return "join";
    }

}