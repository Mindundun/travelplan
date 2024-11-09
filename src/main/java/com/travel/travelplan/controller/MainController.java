package com.travel.travelplan.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.travel.travelplan.components.NaverBlogSearchComponent;
import com.travel.travelplan.dto.CustomUserDetails;
import com.travel.travelplan.dto.naver.BlogPost;
import com.travel.travelplan.entity.User;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class MainController {
    
    private final NaverBlogSearchComponent naverBlogSearchComponent;
    // Logger 선언
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String mainP(Model model){

        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        //UserEntity의 객체를 가져옴
        try {
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
        } catch (Exception e) {
            // Null or ClassCastException 뜸니다 수정하셔야 합니다.
        }
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

    @GetMapping("/test")
    public String test1() {
        return "test1";
    }

    @GetMapping("/test2")
    public String test2() {
        return "test/test";
    }

    @GetMapping("/todomain2")
    public String todomain2() {
        return "todomain2";
    }
    @GetMapping("/travellist")
    public String travellist() {
        return "travellist";
    }
}
