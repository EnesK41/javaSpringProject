package com.example.demo.dto;

import lombok.Data;

@Data
public class GetUserInfoDTO {
    public GetUserInfoDTO(Long id2, String name2, int points) {
        id = id2;
        name = name2;
        point = points;
    }
    long id;
    String name;
    int point;
}
