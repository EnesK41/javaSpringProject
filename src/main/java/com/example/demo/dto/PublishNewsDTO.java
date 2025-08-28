package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PublishNewsDTO {
    @NotBlank(message = "Title can't be empty!")
    private String title;
    @NotBlank(message = "Content can't be empty!")
    private String content;
    private String country;
    private String category;
    private String city;
}
