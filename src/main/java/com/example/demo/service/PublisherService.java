package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.Publisher;
import com.example.demo.repository.PublisherRepository;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final NewsService newsService;

    //@Autowired - Spring automatically inject dependency when there is only one constructor
    public PublisherService(PublisherRepository publisherRepository, NewsService newsService) {
        this.publisherRepository = publisherRepository;
        this.newsService = newsService;
    }

    public void savePublisher(Publisher publisher){
        publisherRepository.save(publisher);
    }

    public void deletePublisher(long id) {
        publisherRepository.deleteById(id);
    }

    //public void updateUser(long id)

    public Optional<Publisher> findPublisherById(long id){
        return publisherRepository.findById(id);
    }

    public List<Publisher> allPublishers(){
        return publisherRepository.findAll();
    }

    public void publishNews(Publisher publisher, News news){
        if(publisher.getNews() == null){
            publisher.setNews(new ArrayList<>());
        }
        publisher.getNews().add(news);
        news.setPublisher(publisher);
        publisherRepository.save(publisher);
    }

    public void deleteNews(Publisher publisher, News news){ // Done for now,testing
        publisher.getNews().remove(news);
        publisherRepository.save(publisher);
        newsService.deleteNews(news);
    }
}
