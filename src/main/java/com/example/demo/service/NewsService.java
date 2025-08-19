package com.example.demo.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.Publisher;
import com.example.demo.repository.NewsRepository;

@Service
public class NewsService {  //Services are singleton that means there is only one of them
    private final NewsRepository newsRepository;
    private final UserService userService;

    //@Autowired - Maybe i will need it at some point, remove in the end
    public NewsService(NewsRepository newsRepository, UserService userService){
        this.newsRepository = newsRepository;
        this.userService = userService;
        
    }

    public List<News> findByCategory(String category){
        return newsRepository.findByCategory(category);
    }

    public List<News> findByCountry(String country){
        return newsRepository.findByCountry(country);
    }

    public List<News> findByCity(String city){
        return newsRepository.findByCity(city);
    }

    public List<News> findByPublisher(Publisher publisher){
        return newsRepository.findByPublisher(publisher);
    }

    public void addNews(News news){ //Could rename it : save - but might be confusing with jpa functionality
        newsRepository.save(news);
    }

    public void deleteNews(News news){  //Rename it : delete
        userService.removeNewsFromBookmarks(news);
        newsRepository.delete(news);
        
    }

    public void incrementViews(Long newsId, Long userId){ //When someone reads the news
        // Fetch the news
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
        
        // Increment views
        news.setViews(news.getViews() + 1);
        newsRepository.save(news);

        // Delegate user points update to UserService
        if (userId != null) {
            userService.incrementPoint(userId);
        }
    }

    public News findById(Long newsId){
        return newsRepository.getReferenceById(newsId);
    }

    /*public void likeNews(News news, User user){}*/

}
