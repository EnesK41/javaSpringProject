package com.example.demo.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Account;
import com.example.demo.entity.CustomUserDetails;
import com.example.demo.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("[DEBUG] Loaded account: " + email + " with password hash: " + account.getPassword());

        return new CustomUserDetails(account);
    }
}
