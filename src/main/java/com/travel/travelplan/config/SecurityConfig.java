package com.travel.travelplan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Autowired
    private CustomLoginFailureHandler customLoginFailureHandler;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() { // 암호화 메소드 구현
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(0)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/").permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()

                // 최초 회원가입 관련 요청은 모두 허용 (로그인 페이지, 회원가입 페이지, 이메일 전송, 이메일 인증, 닉네임 중복체크)
                .requestMatchers("/login", "/join").permitAll()
                .requestMatchers("/todolist/**").permitAll()
                .requestMatchers("/todomain/**").permitAll()
                .requestMatchers("/admin/**").permitAll()// .hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());

        http
            .formLogin((auth) -> auth.loginPage("/login")
                .successHandler(customLoginSuccessHandler)
                .failureHandler(customLoginFailureHandler)
                .permitAll())

            .logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=logout") // 로그아웃 성공시 이동할 페이지
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .permitAll());

        http
            .csrf((auth) -> auth.disable());

        http
            .logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        return http.build();

    }

}
