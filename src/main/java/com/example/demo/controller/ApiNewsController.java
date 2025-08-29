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
            // The controller accepts the filter parameters...
            @RequestParam(defaultValue = "latest headlines") String query,
            @RequestParam(defaultValue = "us") String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        // ...and passes them directly to the service.
        Page<ApiNewsDTO> newsPage = apiNewsService.getNews(query, country, page, size);
        return ResponseEntity.ok(newsPage);
    }
}