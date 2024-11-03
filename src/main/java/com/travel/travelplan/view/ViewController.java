package com.travel.travelplan.view;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        log.info("login");
        return "login";
    }

}
