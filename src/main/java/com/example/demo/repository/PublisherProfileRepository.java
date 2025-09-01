package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PublisherProfile;


@Repository
public interface PublisherProfileRepository extends JpaRepository<PublisherProfile, Long> {
    Optional<PublisherProfile> findByAccount_Id(Long accountId);
}
