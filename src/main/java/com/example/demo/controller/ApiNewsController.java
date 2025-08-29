package com.example.demo.controller;

import com.example.demo.dto.ApiNewsDTO;

import com.example.demo.service.ApiNewsService;
import com.example.demo.service.NewsService;



import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
/* 
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.entity.CustomUserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;
import org.springframework.security.core.Authentication;
*/
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class ApiNewsController {

    private final ApiNewsService apiNewsService;
    //private final NewsService newsService;

    public ApiNewsController(ApiNewsService apiNewsService, NewsService newsService) {
        this.apiNewsService = apiNewsService;
        //this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<ApiNewsDTO>> getNews(
            @RequestParam(defaultValue = "latest headlines") String query,
            @RequestParam(defaultValue = "us") String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ApiNewsDTO> newsPage = apiNewsService.getNews(query, country, page, size);
        return ResponseEntity.ok(newsPage);
    }

    /*@PostMapping("/{newsId}/view")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> recordNewsView(@PathVariable Long newsId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long accountId = userDetails.getAccount().getId();

        try {
            // Note: This will only work for local news (News entity), not ApiArticle.
            // We may need to adjust the service logic later to handle both.
            newsService.openNews(newsId, accountId);
            return ResponseEntity.ok(Map.of("message", "View recorded and points awarded."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }*/
}