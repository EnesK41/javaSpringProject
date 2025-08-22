package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "publisher_profile")
@Data
public class PublisherProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    private long points;

    @OneToMany(mappedBy = "publisher")
    private Set<News> news = new HashSet<>();
}
