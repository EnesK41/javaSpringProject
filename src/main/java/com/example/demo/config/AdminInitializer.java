package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(accountRepository.findByEmail("admin@admin.com").isEmpty()) {
            Account adminAccount = new Account();
            adminAccount.setName("Admin");
            adminAccount.setEmail("admin@admin.com");
            adminAccount.setPassword(passwordEncoder.encode("admin"));
            adminAccount.setRole(Account.Role.ADMIN);

            accountRepository.save(adminAccount);
            System.out.println("Admin account created: admin@admin.com / admin");
        }
    }
}
