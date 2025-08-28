package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.GetUserInfoDTO;
import com.example.demo.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/{id}/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUserInfoDTO> getUserInfo(@PathVariable Long id){
        GetUserInfoDTO dto = userService.getUserInfo(id);
        return ResponseEntity.ok(dto);
    }

    /*@GetMapping("/{id}/bookmarks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUserInfoDTO> getBookmarks(@PathVariable Long id){
        GetUserInfoDTO dto = userService.getUserInfo(id);
        return ResponseEntity.ok(dto);
    }*/
    //public void like(){}
}
