package com.example.demo.repository;

import com.example.demo.entity.ApiNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApiNewsRepository extends JpaRepository<ApiNews, Long> {
    // A helper method to check if an article already exists
    Optional<ApiNews> findByUrl(String url);
}