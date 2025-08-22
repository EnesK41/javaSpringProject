package com.example.demo.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.repository.NewsRepository;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserService userService;

    public NewsService(NewsRepository newsRepository, UserService userService) {
        this.newsRepository = newsRepository;
        this.userService = userService;
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

    public News findById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }
}
