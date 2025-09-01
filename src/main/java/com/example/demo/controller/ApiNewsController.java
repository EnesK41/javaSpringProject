package com.example.demo.controller;

import com.example.demo.dto.ApiNewsDTO;
import com.example.demo.entity.CustomUserDetails;
import com.example.demo.service.ApiNewsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class ApiNewsController {

    private final ApiNewsService apiNewsService;

    public ApiNewsController(ApiNewsService apiNewsService) {
        this.apiNewsService = apiNewsService;
    }

    @GetMapping
    public ResponseEntity<Page<ApiNewsDTO>> getNews(
            @RequestParam(defaultValue = "haber") String query,
            @RequestParam(defaultValue = "tr") String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {

        Page<ApiNewsDTO> newsPage = apiNewsService.getNews(query, country, page, size);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiNewsDTO> getApiNewsById(@PathVariable Long id) {
        ApiNewsDTO articleDto = apiNewsService.getApiNewsById(id);
        return ResponseEntity.ok(articleDto);
    }
    
    @PostMapping("/{newsId}/view")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> recordNewsView(@PathVariable Long newsId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long accountId = userDetails.getAccount().getId();

        try {
            apiNewsService.recordApiNewsViewAndAwardPointToUser(newsId, accountId);
            return ResponseEntity.ok(Map.of("message", "Point awarded for viewing API news."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}