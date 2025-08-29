package com.example.demo.dto;

import com.example.demo.entity.News;
import lombok.Data;

@Data
public class NewsDTO {
    private long id;
    private String title;
    private String content;
    private String country;
    private String category;
    private String city;
    private String url;
    private long views;
    private String publisherName;

    public NewsDTO(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.content = news.getContent();
        this.country = news.getCountry();
        this.category = news.getCategory();
        this.city = news.getCity();
        this.url = news.getUrl();
        this.views = news.getViews();
        if (news.getPublisher() != null && news.getPublisher().getAccount() != null) {
            this.publisherName = news.getPublisher().getAccount().getName();
        } else {
            this.publisherName = "Unknown Author";
        }
    }
}