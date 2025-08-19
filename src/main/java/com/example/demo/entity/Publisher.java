package com.example.demo.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Publisher extends Account {
    private long points;
    @OneToMany(mappedBy = "publisher")
    private Set<News> news;
}
