package com.example.demo.entity;

import java.util.List;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Account{
    private long points;
    private String country;
    private String city;
    private String category;
    @OneToMany(mappedBy = "user")
    private List<News> bookmarks;
    public Object getBookmarks; 

}
