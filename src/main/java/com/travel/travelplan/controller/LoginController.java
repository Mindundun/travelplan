package com.travel.travelplan.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.travel.travelplan.dto.CustomUserDetails;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인된 사용자라면 리디렉션
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails;
        if (isLoggedIn) {
            return "redirect:/";
        }

        return "login";
    }
    
    
}
