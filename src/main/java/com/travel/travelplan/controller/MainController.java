package com.travel.travelplan.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.travel.travelplan.components.NaverBlogSearchComponent;
import com.travel.travelplan.dto.naver.BlogPost;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    
    private final NaverBlogSearchComponent naverBlogSearchComponent;

    @GetMapping("/")
    public String mainP(Model model){

        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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

    @GetMapping("/travel-book")
    @ResponseBody
    public ResponseEntity<List<BlogPost>> travelBook() {
        try {
            List<BlogPost> blogPosts = naverBlogSearchComponent.searchBlog("여행");
            return ResponseEntity.ok().body(blogPosts);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
