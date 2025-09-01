package com.example.demo.service;

import com.example.demo.dto.NewsDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.PublisherProfileRepository;
import com.example.demo.repository.UserProfileRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserProfileRepository userProfileRepository;
    private final PublisherProfileRepository publisherProfileRepository;

    public NewsService(NewsRepository newsRepository,
                       UserProfileRepository userProfileRepository,
                       PublisherProfileRepository publisherProfileRepository) {
        this.newsRepository = newsRepository;
        this.userProfileRepository = userProfileRepository;
        this.publisherProfileRepository = publisherProfileRepository;
    }

    @Transactional
    public void openNews(Long newsId, Long viewingUserAccountId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));
        news.setViews(news.getViews() + 1);

        UserProfile viewingUser = userProfileRepository.findByAccount_Id(viewingUserAccountId)
                .orElseThrow(() -> new RuntimeException("Viewing user not found with Account ID: " + viewingUserAccountId));
        viewingUser.setPoints(viewingUser.getPoints() + 1);

        PublisherProfile publisher = news.getPublisher();
        if (publisher != null) {
            publisher.setPoints(publisher.getPoints() + 1);
            publisherProfileRepository.save(publisher);
        }

        newsRepository.save(news);
        userProfileRepository.save(viewingUser);
    }

    @Transactional(readOnly = true)
    public Page<NewsDTO> getAllLocalNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return newsRepository.findAllWithPublisherAndAccount(pageable)
                .map(NewsDTO::new);
    }

    public News findById(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
    }

    @Transactional(readOnly = true)
    public NewsDTO getLocalNewsById(Long id) {
        News news = newsRepository.findByIdWithPublisherAndAccount(id)
                .orElseThrow(() -> new RuntimeException("News not found with ID: " + id));
        return new NewsDTO(news);
    }
}