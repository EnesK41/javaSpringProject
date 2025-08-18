package com.example.demo.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
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
        return newsRepository.findByCountry(city);
    }

    public List<News> findByPublisher(String publisher){
        return newsRepository.findByCountry(publisher);
    }

    public void addNews(News news){
        newsRepository.save(news);
    }

    public void deleteNews(News news){
        newsRepository.delete(news);
    }

    public void openNews(News news, User user){
        news.setViews(news.getViews()+1);
        newsRepository.save(news);

        userService.openNews(user,news);
    }

    /*public void likeNews(News news, User user){}*/

}
