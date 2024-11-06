package com.travel.travelplan.controller;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.travel.travelplan.dto.CustomUserDetails;
import com.travel.travelplan.entity.User;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String mainP(Model model){

        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        //UserEntity의 객체를 가져옴
        CustomUserDetails customuserdetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customuserdetails.getUser();

        // User 객체에서 id를 가져옴
        int userId = user.getId(); // getId() 메서드를 사용하여 ID를 가져옵니다.
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("id", id );
        //이 role 값에 따라 분기처리 가능 : admin만 삭제하기 등
        model.addAttribute("role",role);
        System.out.println("ID: " + id); // 로그로 ID 확인

        return "index";
        // 민경아 인덱스로 바꾸래서 바꿨다 기존:main
    }
}
