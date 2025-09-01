package com.example.demo.service;

import com.example.demo.dto.ApiNewsDTO;
import com.example.demo.entity.ApiNews;
import com.example.demo.entity.UserProfile;
import com.example.demo.repository.ApiNewsRepository;
import com.example.demo.repository.UserProfileRepository; // 1. Import the missing repository
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApiNewsService {

    private final ApiNewsRepository apiNewsRepository;
    private final UserProfileRepository userProfileRepository; // 2. Add the repository as a dependency
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${brave.api.key}")
    private String braveApiKey;

    private final Map<String, LocalDateTime> queryCacheTimestamps = new ConcurrentHashMap<>();

    // 3. Update the constructor to accept the new dependency
    public ApiNewsService(ApiNewsRepository apiNewsRepository, UserProfileRepository userProfileRepository) {
        this.apiNewsRepository = apiNewsRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public Page<ApiNewsDTO> getNews(String query, String country, int page, int size) {
        String cacheKey = query + ":" + country;
        LocalDateTime lastFetch = queryCacheTimestamps.getOrDefault(cacheKey, LocalDateTime.MIN);

        if (lastFetch.isBefore(LocalDateTime.now().minusHours(1))) {
            fetchAndStoreFromBraveApi(query, country);
            queryCacheTimestamps.put(cacheKey, LocalDateTime.now());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        
        if ("*".equals(query)) {
            // If the query is a wildcard, fetch all articles.
            return apiNewsRepository.findAll(pageable).map(ApiNewsDTO::new);
        } else {
            // Otherwise, perform the specific search.
            return apiNewsRepository.findByQuery(query, pageable).map(ApiNewsDTO::new);
        }
    }

    private void fetchAndStoreFromBraveApi(String query, String country) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.search.brave.com/res/v1/news/search")
                .queryParam("q", query)
                .queryParam("country", country)
                .queryParam("count", 50)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Subscription-Token", braveApiKey);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("results");

            if (results.isArray()) {
                for (JsonNode articleNode : results) {
                    String articleUrl = articleNode.path("url").asText();
                    if (apiNewsRepository.findByUrl(articleUrl).isEmpty()) {
                        ApiNews article = new ApiNews();
                        article.setTitle(articleNode.path("title").asText());
                        article.setDescription(articleNode.path("description").asText());
                        article.setUrl(articleUrl);
                        article.setSource(articleNode.path("meta_url").path("hostname").asText());
                        article.setImageUrl(articleNode.path("thumbnail").path("src").asText(null));
                        String dateString = articleNode.path("page_age").asText(null);
                        if (dateString != null && !dateString.isEmpty()) {
                            article.setPublishedAt(LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                        }
                        apiNewsRepository.save(article);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch news from Brave API: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public ApiNewsDTO getApiNewsById(Long id) {
        ApiNews article = apiNewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API News article not found with ID: " + id));
        return new ApiNewsDTO(article);
    }

    @Transactional
    // 4. Update the method to accept the news ID (even though we don't use it, it's good practice)
    public void recordApiNewsViewAndAwardPointToUser(Long apiNewsId, Long viewingUserAccountId) {
        UserProfile viewingUser = userProfileRepository.findByAccount_Id(viewingUserAccountId)
                .orElseThrow(() -> new RuntimeException("Viewing user not found with Account ID: " + viewingUserAccountId));
        
        viewingUser.setPoints(viewingUser.getPoints() + 1);

        userProfileRepository.save(viewingUser);
    }
}