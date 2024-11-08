package com.travel.travelplan.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travel.travelplan.dto.CustomUserDetails;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP(@RequestParam(value = "redirectUrl", required = false) String redirectUrl, @RequestParam(value = "error", required = false) String error, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인된 사용자라면 리디렉션
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails;
        if (isLoggedIn) {
            return "redirect:/";
        }

        if(error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if(redirectUrl != null) {
            model.addAttribute("redirectUrl", redirectUrl);
        }

        return "login";
    }
    
    
}
