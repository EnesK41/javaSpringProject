package com.example.demo.service;

import com.example.demo.dto.GetPublisherInfoDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.repository.PublisherProfileRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherProfileRepository publisherProfileRepository;
    private final NewsService newsService;

    public PublisherService(PublisherProfileRepository publisherProfileRepository, @Lazy NewsService newsService) {
        this.publisherProfileRepository = publisherProfileRepository;
        this.newsService = newsService;
    }


    @Transactional(readOnly = true) 
    public GetPublisherInfoDTO getPublisherInfo(Long accountId) {
        PublisherProfile publisher = publisherProfileRepository.findByAccount_Id(accountId)
            .orElseThrow(() -> new RuntimeException("Publisher not found for account ID: " + accountId)); // Or a custom not-found exception

        // The business logic of creating the DTO is now correctly in the service.
        return new GetPublisherInfoDTO(
            publisher.getId(),
            publisher.getAccount().getName(),
            publisher.getPoints(),
            publisher.getNews().size()
        );
    }
    // -----------------------------


    public void savePublisher(PublisherProfile publisher){
        publisherProfileRepository.save(publisher);
    }

    public void deletePublisher(long id){
        publisherProfileRepository.deleteById(id);
    }

    public Optional<PublisherProfile> findPublisherById(long id){
        return publisherProfileRepository.findById(id);
    }

    public List<PublisherProfile> allPublishers(){
        return publisherProfileRepository.findAll();
    }

    @Transactional // Add this to ensure data consistency
    public void publishNews(Long publisherId, News news){
        PublisherProfile publisher = publisherProfileRepository.findById(publisherId)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));

        publisher.getNews().add(news);
        // You don't need to save the publisher here; the transaction will handle it.

        news.setPublisher(publisher);
        newsService.addNews(news);
    }

    @Transactional // Add this to ensure data consistency
    public void deleteNews(Long publisherId, Long newsId){
        PublisherProfile publisher = publisherProfileRepository.findById(publisherId)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));

        News news = newsService.findById(newsId);
        publisher.getNews().remove(news);
        // You don't need to save the publisher here; the transaction will handle it.

        newsService.deleteNews(news);
    }
}