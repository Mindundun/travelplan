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
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable())
            .securityMatcher("/api/**")
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // GET 요청은 모두 허용
                .anyRequest().authenticated());

        return http.build();
    }

}
