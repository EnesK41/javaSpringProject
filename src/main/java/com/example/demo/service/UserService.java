package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.GetUserInfoDTO;
import com.example.demo.entity.News;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.UserProfileRepository;

import org.springframework.transaction.annotation.Transactional; 

@Service
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final NewsRepository newsRepository; // <-- Change this

    public UserService(UserProfileRepository userProfileRepository, NewsRepository newsRepository) { // <-- Change this
        this.userProfileRepository = userProfileRepository;
        this.newsRepository = newsRepository; // <-- Change this
    }   

    public void deleteUser(Long id){
        userProfileRepository.deleteById(id);
    }

    public Optional<UserProfile> findUserByEmail(String email){
        return userProfileRepository.findByAccount_Email(email);
    }

    public List<UserProfile> allUsers(){
        return userProfileRepository.findAll();
    }

    public void incrementPoint(Long userId){
        UserProfile user = userProfileRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoints(user.getPoints() + 1);
        userProfileRepository.save(user);
    }

    @Transactional
    public void addBookmark(Long userAccountId, Long newsId) {
        UserProfile user = userProfileRepository.findById(userAccountId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // FIX: Finds the news article directly from the repository.
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
        user.getBookmarks().add(news);
        userProfileRepository.save(user);
    }

    @Transactional
    public void removeBookmark(Long userAccountId, Long newsId) {
        UserProfile user = userProfileRepository.findById(userAccountId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // FIX: Finds the news article directly from the repository.
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
        user.getBookmarks().remove(news);
        userProfileRepository.save(user);
    }

    @Transactional
    public void removeNewsFromBookmarks(News news) {
        List<UserProfile> users = userProfileRepository.findAllByBookmarks_Id(news.getId());
        for (UserProfile user : users) {
            user.getBookmarks().remove(news);
        }
        userProfileRepository.saveAll(users);
    }
    
    @Transactional(readOnly = true)
    public GetUserInfoDTO getUserInfo(long accountId){
        UserProfile user = userProfileRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("User not found for account ID: " + accountId));
        return new GetUserInfoDTO(
                user.getId(),
                user.getAccount().getName(),
                user.getPoints()
        );
    }
}
