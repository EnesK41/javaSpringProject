package com.example.demo.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    String email;
    String password;
    String name;
    String role;
}
