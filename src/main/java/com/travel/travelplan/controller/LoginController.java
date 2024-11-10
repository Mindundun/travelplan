package com.travel.travelplan.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travel.travelplan.dto.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
        @RequestParam(value = "error", required = false) String error,
        Model model
    ) {
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
            request.getSession().setAttribute("redirectUrl", redirectUrl);
        } else {
            // redirectUrl이 없으면 SavedRequest에서 가져오기
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
            if (savedRequest != null) {
                // SavedRequest에서 URL을 가져와 세션에 저장
                String targetUrl = savedRequest.getRedirectUrl();
                if(!targetUrl.contains("error?continue")) { // 하.. 임시조치.. 이거 어떻게 해결해야할지 모르겠다.
                    request.getSession().setAttribute("redirectUrl", targetUrl);
                }
            }
        }

        return "login";
    }
    
    
}
