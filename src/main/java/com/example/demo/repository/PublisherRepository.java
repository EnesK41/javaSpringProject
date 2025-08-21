package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Publisher;


@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByEmail(String mail);
    // Additional query methods can be defined here if needed
    // It seems Jpa handles most operations so no need for anything 
}
