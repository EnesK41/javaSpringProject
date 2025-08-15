package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @GetMapping("/login")
    public void login(String email, String password){}
    
    @GetMapping("/register")
    public void register(){}// Maybe email authentication?

    public void like(){}
}
