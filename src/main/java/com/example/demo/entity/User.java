package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "app_user")
public class User extends Account{
    private long points = 0;
    private String country;
    private String city;
    private String category;
    @ManyToMany
    @JoinTable(
        name = "user_bookmarks",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "news_id", referencedColumnName = "id")
    )
    private Set<News> bookmarks = new HashSet<>();

}
