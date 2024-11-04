package com.travel.travelplan.components;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travelplan.dto.naver.BlogPost;
import com.travel.travelplan.dto.naver.NaverBlogDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableCaching
public class NaverBlogSearchComponent {

    private final String BASE_URL = "https://openapi.naver.com/v1/search/blog.json";

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Cacheable("naverBlog")
    public List<BlogPost> searchBlog(String query){
        HttpClient client = HttpClient.newHttpClient();

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "?query=" + encodedQuery + "&display=100" + "&sort=sim"))
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            log.error("URI Syntax Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String responseBody = response.body();
        log.debug("responseBody: {}", responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        NaverBlogDto naverBlogDto;
        try {
            naverBlogDto = objectMapper.readValue(responseBody, NaverBlogDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.debug("naverBlogDto: {}", naverBlogDto);

        return naverBlogDto.getItems();
    }

}
