package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Publisher extends Account {
    private long points = 0;        //For news getting liked/or viewed. Not decided yet.
    @OneToMany(mappedBy = "publisher")
    private Set<News> news = new HashSet<>();
}
