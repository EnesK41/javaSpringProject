package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile {
    @Id
    private Long id; 

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    private int points;
    private String country;
    private String city;
    private String category;

    @ManyToMany
    @JoinTable(
        name = "user_bookmarks",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    private Set<News> bookmarks = new HashSet<>();
}
