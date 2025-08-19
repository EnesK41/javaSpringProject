package com.example.demo.service;

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

    public void publishNews(Long publisherId, News news){
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow();

        publisher.getNews().add(news);
        publisherRepository.save(publisher);
        newsService.addNews(news);

    }

    public void deleteNews(Long publisherId, Long newsId){ // Done for now,testing
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow();
                    
        News news = newsService.findById(newsId);
        publisher.getNews().remove(news);
        publisherRepository.save(publisher);

        newsService.deleteNews(news);
    }
}
