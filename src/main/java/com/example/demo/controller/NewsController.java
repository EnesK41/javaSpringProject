package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.CustomUserDetails;
import com.example.demo.service.NewsService;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/News")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService){
        this.newsService = newsService;
    }

    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> openNews(@PathVariable Long id, Authentication authentication){
        CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
        Long userId = user.getAccount().getId();
        newsService.openNews(id, userId);
        return ResponseEntity.noContent().build();
    }
}
