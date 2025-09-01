package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCountry(String country);
    List<News> findByCity(String city);
    List<News> findByCategory(String category);
    List<News> findByPublisher(PublisherProfile publisher);
    @Query("SELECT n FROM News n LEFT JOIN FETCH n.publisher p LEFT JOIN FETCH p.account")
    Page<News> findAllWithPublisherAndAccount(Pageable pageable);

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.publisher p LEFT JOIN FETCH p.account WHERE n.id = :id")
    Optional<News> findByIdWithPublisherAndAccount(@Param("id") Long id);
}
    