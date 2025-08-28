package com.example.demo.dto;

import com.example.demo.entity.ApiNews;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiNewsDTO {
    private Long id;
    private String url;
    private String title;
    private String description;
    private String source;
    private String imageUrl;
    private LocalDateTime publishedAt;

    public ApiNewsDTO(ApiNews entity) {
        this.id = entity.getId();
        this.url = entity.getUrl();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.source = entity.getSource();
        this.imageUrl = entity.getImageUrl();
        this.publishedAt = entity.getPublishedAt();
    }
}