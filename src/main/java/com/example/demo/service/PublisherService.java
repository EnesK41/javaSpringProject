package com.example.demo.service;

import java.util.List;
import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Publisher;
import com.example.demo.repository.PublisherRepository;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    //@Autowired - Spring automatically inject dependency when there is only one constructor
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
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
}
