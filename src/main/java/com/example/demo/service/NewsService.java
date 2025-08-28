package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PublisherProfileRepository;
import com.example.demo.repository.UserProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserService userService;
    private final UserProfileRepository userProfileRepository;
    private final PublisherProfileRepository publisherProfileRepository;

    public NewsService(NewsRepository newsRepository, UserService userService,
                        UserProfileRepository userProfileRepository,
                        PublisherProfileRepository publisherProfileRepository) {
        this.newsRepository = newsRepository;
        this.userService = userService;
        this.userProfileRepository = userProfileRepository;
        this.publisherProfileRepository = publisherProfileRepository;
    }

    public List<News> findByCategory(String category) {
        return newsRepository.findByCategory(category);
    }

    public List<News> findByCountry(String country) {
        return newsRepository.findByCountry(country);
    }

    public List<News> findByCity(String city) {
        return newsRepository.findByCity(city);
    }

    public List<News> findByPublisher(PublisherProfile publisher) {
        return newsRepository.findByPublisher(publisher);
    }

    public void addNews(News news) {
        newsRepository.save(news);
    }

    public void deleteNews(News news) {
        userService.removeNewsFromBookmarks(news);
        newsRepository.delete(news);
    }

    public void incrementViews(Long newsId, Long userId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        news.setViews(news.getViews() + 1);
        newsRepository.save(news);

        if (userId != null) {
            userService.incrementPoint(userId);
        }
    }

    @Transactional
    public void openNews(Long newsId, Long viewingUserAccountId) {

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));
        news.setViews(news.getViews() + 1);


        UserProfile viewingUser = userProfileRepository.findById(viewingUserAccountId)
                .orElseThrow(() -> new RuntimeException("Viewing user not found with Account ID: " + viewingUserAccountId));
        viewingUser.setPoints(viewingUser.getPoints() + 1); //Just 1 point, no need to complicate things

        PublisherProfile publisher = news.getPublisher();
        if (publisher != null) {
            publisher.setPoints(publisher.getPoints() + 1);
            publisherProfileRepository.save(publisher);
        }


        newsRepository.save(news);
        userProfileRepository.save(viewingUser);
    }

    public News findById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }
}
