package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ApiNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000,unique = true) // Ensures we don't save the same article twice
    private String url;

    private String title;

    @Column(length = 1000) // Give more space for descriptions
    private String description;

    private String source; // e.g., "Reuters"

    @Column(length = 1000)
    private String imageUrl;
    
    private LocalDateTime publishedAt;
}