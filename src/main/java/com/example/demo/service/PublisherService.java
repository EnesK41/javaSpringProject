package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.repository.PublisherProfileRepository;

@Service
public class PublisherService {

    private final PublisherProfileRepository publisherProfileRepository;
    private final NewsService newsService;

    public PublisherService(PublisherProfileRepository publisherProfileRepository, @Lazy NewsService newsService) {
        this.publisherProfileRepository = publisherProfileRepository;
        this.newsService = newsService;
    }

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

    public void publishNews(Long publisherId, News news){
        PublisherProfile publisher = publisherProfileRepository.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        publisher.getNews().add(news);
        publisherProfileRepository.save(publisher);

        news.setPublisher(publisher);
        newsService.addNews(news);
    }

    public void deleteNews(Long publisherId, Long newsId){
        PublisherProfile publisher = publisherProfileRepository.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        News news = newsService.findById(newsId);
        publisher.getNews().remove(news);
        publisherProfileRepository.save(publisher);

        newsService.deleteNews(news);
    }
}
