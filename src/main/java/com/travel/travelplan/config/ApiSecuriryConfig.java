package com.travel.travelplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApiSecuriryConfig {

    @Bean
    @Order(0)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable())
            .securityMatcher("/api/**")
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // GET 요청은 모두 허용
                .requestMatchers(HttpMethod.POST, "/api/v1/join").permitAll() // POST 요청은 회원가입 API만 허용
                .requestMatchers(HttpMethod.POST, "/api/v1/send/email").permitAll() // POST 요청은 이메일 전송 API만 허용
                .requestMatchers(HttpMethod.POST, "/api/v1/verify/email").permitAll() // POST 요청은 이메일 인증 API만 허용
                .anyRequest().authenticated());

        return http.build();
    }

}
