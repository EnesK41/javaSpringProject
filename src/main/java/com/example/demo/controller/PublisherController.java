package com.example.demo.controller;

import com.example.demo.dto.PublishNewsDTO;
import com.example.demo.dto.GetPublisherInfoDTO;
import com.example.demo.dto.NewsDTO;
import com.example.demo.entity.CustomUserDetails;
import com.example.demo.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    @GetMapping("/{id}/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetPublisherInfoDTO> getPublisherInfo(@PathVariable Long id){
        GetPublisherInfoDTO dto = publisherService.getPublisherInfo(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{publisherId}/news")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NewsDTO>> getNewsByPublisher(@PathVariable Long publisherId) {
        List<NewsDTO> newsList = publisherService.getNewsByPublisher(publisherId);
        return ResponseEntity.ok(newsList);
    }

    @PostMapping("/news")
    @PreAuthorize("hasRole('PUBLISHER')")
    public ResponseEntity<NewsDTO> publishNews(@Valid @RequestBody PublishNewsDTO dto, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long accountId = userDetails.getAccount().getId();
        NewsDTO createdNews = publisherService.publishNews(accountId, dto);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    @DeleteMapping("/news/{newsId}")
    @PreAuthorize("hasRole('PUBLISHER') || hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long newsId, Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long accountId = userDetails.getAccount().getId();
        publisherService.deleteNews(newsId, accountId, userDetails.getAuthorities());
        return ResponseEntity.noContent().build();
    }
}