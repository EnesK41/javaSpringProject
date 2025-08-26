package com.example.demo.dto;

import lombok.Data;

@Data
public class GetPublisherInfoDTO {
    public GetPublisherInfoDTO(Long id2, String name2, long points2, int size) {
         id = id2;
         name = name2;
         points = points2;
         newsCount = size;
    }
    private Long id;
    String name;
    Long points;
    Integer newsCount;
}
