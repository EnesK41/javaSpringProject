package com.example.demo.service;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.repository.NewsRepository;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    //@Autowired - Maybe i will need it at some point, remove in the end
    public NewsService(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
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
}
