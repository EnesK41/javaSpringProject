package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final NewsService newsService;

    //@Autowired - If more than one constructor use autowired   
    public UserService(UserRepository userRepository,@Lazy NewsService newsService) {
        this.userRepository = userRepository;
        this.newsService = newsService;
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

    public void incrementPoint(Long userId){    //When you open the news
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoints(user.getPoints() + 1);
        userRepository.save(user);
    }

    /*public void likeNews(User user, News news){
        
    }*/

    public void addBookmark(Long userId, Long newsId){//Later itcanbe called from newsService to show num of bookmarks
        User user = userRepository.findById(userId).get();
        News news = newsService.findById(newsId);
        user.getBookmarks().add(news);
        userRepository.save(user);
    }

    public void removeBookmark(Long userId, Long newsId){
        User user = userRepository.getReferenceById(userId);
        News news = new News();
        news = newsService.findById(newsId);
        user.getBookmarks().remove(news);
        userRepository.save(user);
    }

    public void removeNewsFromBookmarks(News news){
        List<User> users = userRepository.findAllByBookmarks_Id(news.getId());

        for (User user : users) {
            user.getBookmarks().remove(news);
        }
        userRepository.saveAll(users);
    }

}
