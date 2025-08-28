package com.example.demo.controller;

import com.example.demo.dto.ApiNewsDTO;
import com.example.demo.service.ApiNewsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class ApiNewsController {

    private final ApiNewsService apiNewsService;

    public ApiNewsController(ApiNewsService apiNewsService) {
        this.apiNewsService = apiNewsService;
    }

    @GetMapping
    public ResponseEntity<Page<ApiNewsDTO>> getNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ApiNewsDTO> newsPage = apiNewsService.getNews(page, size);
        return ResponseEntity.ok(newsPage);
    }
}
