package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CustomUserDetails;
import com.example.demo.repository.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PublisherRepository publisherRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(UserRepository userRepository,
                                    PublisherRepository publisherRepository,
                                    AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.publisherRepository = publisherRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (adminRepository.findByEmail(email).isPresent()) {
            return new CustomUserDetails(adminRepository.findByEmail(email).get());
        } else if (publisherRepository.findByEmail(email).isPresent()) {
            return new CustomUserDetails(publisherRepository.findByEmail(email).get());
        } else if (userRepository.findByEmail(email).isPresent()) {
            return new CustomUserDetails(userRepository.findByEmail(email).get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}

