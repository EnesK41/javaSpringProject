package com.example.demo.service;

import com.example.demo.dto.ApiNewsDTO;
import com.example.demo.entity.ApiNews;
import com.example.demo.repository.ApiNewsRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ApiNewsService {

    private final ApiNewsRepository ApiNewsRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${brave.api.key}")
    private String braveApiKey;

    // This holds the timestamp of the last successful API fetch.
    // AtomicReference is used to ensure it's safe in a multi-threaded environment.
    private final AtomicReference<LocalDateTime> lastFetchTime = new AtomicReference<>(LocalDateTime.MIN);

    public ApiNewsService(ApiNewsRepository ApiNewsRepository) {
        this.ApiNewsRepository = ApiNewsRepository;
    }

    @Transactional(readOnly = true)
    public Page<ApiNewsDTO> getNews(int page, int size) {

        if (lastFetchTime.get().isBefore(LocalDateTime.now().minusHours(1))) {
            fetchAndStoreFromBraveApi();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());

        return ApiNewsRepository.findAll(pageable)
                .map(ApiNewsDTO::new);
    }
    
    @Transactional
    public void fetchAndStoreFromBraveApi() {
        String url = "https://api.search.brave.com/res/v1/news/search?q=latest+headlines&count=50";

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
                    if (ApiNewsRepository.findByUrl(articleUrl).isEmpty()) {
                        ApiNews article = new ApiNews();
                        article.setTitle(articleNode.path("title").asText());
                        article.setDescription(articleNode.path("description").asText());
                        article.setUrl(articleUrl);
                        article.setSource(articleNode.path("meta_url").path("hostname").asText());
                        article.setImageUrl(articleNode.path("thumbnail").path("src").asText(null));

                        String dateString = articleNode.path("page_age").asText(null);
                        if (dateString != null) {
                            article.setPublishedAt(LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                        }
                        
                        ApiNewsRepository.save(article);
                    }
                }
            }
            lastFetchTime.set(LocalDateTime.now());

        } catch (Exception e) {
            System.err.println("Failed to fetch news from Brave API: " + e.getMessage());
        }
    }
}