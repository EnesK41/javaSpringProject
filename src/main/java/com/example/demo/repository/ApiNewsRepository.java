package com.example.demo.repository;

import com.example.demo.entity.ApiNews;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApiNewsRepository extends JpaRepository<ApiNews, Long> {
    // A helper method to check if an article already exists
    Optional<ApiNews> findByUrl(String url);
    @Query("SELECT a FROM ApiNews a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ApiNews> findByQuery(@Param("query") String query, Pageable pageable);
}