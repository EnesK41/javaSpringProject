package com.example.demo.entity;

import java.util.List;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long points;
    private String country;
    private String city;
    private String email;
    private String password;
    private String category;
    @OneToMany(mappedBy = "user")
    private List<News> bookmarks;
    
}
