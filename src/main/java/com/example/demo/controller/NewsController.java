package com.example.demo.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NewsDTO;
import com.example.demo.entity.CustomUserDetails;
import com.example.demo.service.NewsService;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getLocalNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        
        Page<NewsDTO> newsPage = newsService.getAllLocalNews(page, size);
        return ResponseEntity.ok(newsPage);
    }

    @PostMapping("/{id}/view")
    // FIX: Changed to isAuthenticated() so any logged-in user (USER or PUBLISHER) can trigger this.
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> recordNewsView(@PathVariable Long id, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getAccount().getId();

        try {
            // Calling your existing service method
            newsService.openNews(id, userId);
            // Return a clear success message to the frontend
            return ResponseEntity.ok(Map.of("message", "View recorded and points awarded."));
        } catch (Exception e) {
            // Return a clear error message if something goes wrong
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
