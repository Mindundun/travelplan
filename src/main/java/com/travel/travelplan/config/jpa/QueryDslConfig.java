package com.travel.travelplan.config.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {

    @Autowired
    private EntityManager em;

    @Bean
    JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

}
