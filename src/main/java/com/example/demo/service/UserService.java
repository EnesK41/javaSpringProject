package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    //@Autowired - If more than one constructor use autowired   
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
        if(userRepository.findByEmail(user.getEmail()) != null){
            return; //Will update
        }
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    //public void updateUser();

    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public void openNews(User user, News news){
        user.setPoints(user.getPoints()+1);
    }

    /*public void likeNews(User user, News news){
        
    }*/

    public void bookmarkNews(User user, News news){
        if(user.getBookmarks() != null){
            user.setBookmarks(new ArrayList<>());
        }    
        user.getBookmarks().add(news);
        userRepository.save(user);
    }

    public void deleteNews(News news){
        for(User user : userRepository.findAll()){
            if(user.getBookmarks.)
        }
    }
}
