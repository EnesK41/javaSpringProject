package com.example.demo.controller;

import com.example.demo.dto.GetPublisherInfoDTO;
import com.example.demo.service.PublisherService; // Import the service
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    // THE FIX: Inject the service (the "chef"), not the repository.
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
}