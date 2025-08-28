package com.example.demo.service;

import com.example.demo.dto.GetPublisherInfoDTO;
import com.example.demo.dto.NewsDTO;
import com.example.demo.dto.PublishNewsDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PublisherProfileRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherProfileRepository publisherProfileRepository;
    private final NewsService newsService;
    private final NewsRepository newsRepository;

    public PublisherService(PublisherProfileRepository publisherProfileRepository, @Lazy NewsService newsService, NewsRepository newsRepository) {
        this.publisherProfileRepository = publisherProfileRepository;
        this.newsService = newsService;
        this.newsRepository = newsRepository;
    }


    @Transactional(readOnly = true) 
    public GetPublisherInfoDTO getPublisherInfo(Long accountId) {
        PublisherProfile publisher = publisherProfileRepository.findByAccount_Id(accountId)
            .orElseThrow(() -> new RuntimeException("Publisher not found for account ID: " + accountId)); 

        return new GetPublisherInfoDTO(
            publisher.getId(),
            publisher.getAccount().getName(),
            publisher.getPoints(),
            publisher.getNews().size()
        );
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

    @Transactional // Add this to ensure data consistency
    public NewsDTO publishNews(Long publisherId, PublishNewsDTO dto){
        PublisherProfile publisher = publisherProfileRepository.findById(publisherId)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));

        News news = new News();
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setCountry(dto.getCountry());
        news.setCategory(dto.getCategory());
        news.setCity(dto.getCity());

        news.setPublisher(publisher);
        newsService.addNews(news);
        news.setUrl("/news/" + System.currentTimeMillis()); 
        news.setViews(0);
        news.setPublisher(publisher);
        News savedNews = newsRepository.save(news);

        return new NewsDTO(savedNews);
    }

    @Transactional 
    public void deleteNews(Long newsId, Long publisherAccountId, Collection<? extends GrantedAuthority> authorities){
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // 3. Security Check: Is the logged-in user the owner of the article OR are they an admin?
        Long ownerAccountId = news.getPublisher().getAccount().getId();
        if (!ownerAccountId.equals(publisherAccountId) && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to delete this article.");
        }

        newsService.deleteNews(news);
    }
}