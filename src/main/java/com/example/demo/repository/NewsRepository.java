package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.News;
import com.example.demo.entity.Publisher;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCountry(String country);
    List<News> findByCity(String city);
    List<News> findByCategory(String category);
    List<News> findByPublisher(Publisher publisher);
}
