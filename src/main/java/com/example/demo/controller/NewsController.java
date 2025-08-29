package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
