package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;
    private String country;
    private String category;
    private String city;
    private String url;

    private long views = 0;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherProfile publisher; // ✅ instead of Publisher
}



