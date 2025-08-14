package com.example.demo.entity;

import java.util.List;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long points;
    @OneToMany(mappedBy = "publisher")
    private List<News> news;
}
