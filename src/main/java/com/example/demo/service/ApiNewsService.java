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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApiNewsService {

    private final ApiNewsRepository apiNewsRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${brave.api.key}")
    private String braveApiKey;

    // The cache logic correctly lives inside the service layer.
    private final Map<String, LocalDateTime> queryCacheTimestamps = new ConcurrentHashMap<>();

    public ApiNewsService(ApiNewsRepository apiNewsRepository) {
        this.apiNewsRepository = apiNewsRepository;
    }

    @Transactional
    public Page<ApiNewsDTO> getNews(String query, String country, int page, int size) {
        // Create a unique key for the cache based on the filter criteria.
        String cacheKey = query + ":" + country;

        // Get the last fetch time for this specific query, or a very old date if it's not in the cache.
        LocalDateTime lastFetch = queryCacheTimestamps.getOrDefault(cacheKey, LocalDateTime.MIN);

        // Check if the cache for this specific query is stale (older than 1 hour).
        if (lastFetch.isBefore(LocalDateTime.now().minusHours(1))) {
            // If stale, fetch new data from the API using the filters.
            fetchAndStoreFromBraveApi(query, country);
            // Update the cache timestamp for this specific query.
            queryCacheTimestamps.put(cacheKey, LocalDateTime.now());
        }

        // Now, serve the requested page from our database.
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return apiNewsRepository.findAll(pageable).map(ApiNewsDTO::new);
    }

    // This method is private as it's only called by the main getNews method.
    private void fetchAndStoreFromBraveApi(String query, String country) {
        // The URL is now built dynamically with the filter parameters.
        String url = UriComponentsBuilder
                .fromUriString("https://api.search.brave.com/res/v1/news/search")
                .queryParam("q", query)
                .queryParam("country", country)
                .queryParam("count", 50) // Fetch a good number of articles to cache
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
                        if (dateString != null) {
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
}