package com.example.demo.service;

import com.example.demo.dto.GetPublisherInfoDTO;
import com.example.demo.dto.NewsDTO;
import com.example.demo.dto.PublishNewsDTO; // Assuming this is your DTO for creating news
import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PublisherProfileRepository;
import com.github.slugify.Slugify;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherProfileRepository publisherProfileRepository;
    private final NewsRepository newsRepository;
    private final UserService userService;
    private final Slugify slugify = Slugify.builder().build();

    public PublisherService(PublisherProfileRepository publisherProfileRepository,
                            NewsRepository newsRepository,
                            UserService userService) {
        this.publisherProfileRepository = publisherProfileRepository;
        this.newsRepository = newsRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public GetPublisherInfoDTO getPublisherInfo(Long accountId) {
        PublisherProfile publisher = publisherProfileRepository.findByAccount_Id(accountId)
                .orElseThrow(() -> new RuntimeException("Publisher not found for account ID: " + accountId));

        int newsSize = newsRepository.findByPublisher(publisher).size();

        return new GetPublisherInfoDTO(
                publisher.getId(),
                publisher.getAccount().getName(),
                publisher.getPoints(),
                newsSize
        );
    }

    @Transactional(readOnly = true)
    public List<NewsDTO> getNewsByPublisher(Long publisherAccountId) {
        PublisherProfile publisher = publisherProfileRepository.findByAccount_Id(publisherAccountId)
                .orElseThrow(() -> new RuntimeException("Publisher not found for account ID: " + publisherAccountId));
        
        return newsRepository.findByPublisher(publisher).stream()
                .map(NewsDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public NewsDTO publishNews(Long publisherAccountId, PublishNewsDTO dto) {
        PublisherProfile publisher = publisherProfileRepository.findByAccount_Id(publisherAccountId)
                .orElseThrow(() -> new RuntimeException("Publisher profile not found"));

        News news = new News();
        news.setTitle(dto.getTitle());
        news.setContent(dto.getContent());
        news.setCountry(dto.getCountry());
        news.setCategory(dto.getCategory());
        news.setCity(dto.getCity());
        news.setPublisher(publisher);
        news.setViews(0);
        
        // FIX: Generate the slug and set the URL *before* saving.
        String slug = slugify.slugify(dto.getTitle());
        news.setUrl("/dispatch/" + slug);

        // FIX: Save the entity only once.
        News savedNews = newsRepository.save(news);
        return new NewsDTO(savedNews);
    }

    @Transactional
    public void deleteNews(Long newsId, Long publisherAccountId, Collection<? extends GrantedAuthority> authorities) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Long ownerAccountId = news.getPublisher().getAccount().getId();
        if (!ownerAccountId.equals(publisherAccountId) && !isAdmin) {
            throw new AccessDeniedException("You do not have permission to delete this article.");
        }

        userService.removeNewsFromBookmarks(news);
        // FIX: The service now deletes directly from the NewsRepository.
        newsRepository.delete(news);
    }
}