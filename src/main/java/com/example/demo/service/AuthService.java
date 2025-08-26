package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.PublisherProfile;
import com.example.demo.entity.UserProfile;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.PublisherProfileRepository;
import com.example.demo.repository.UserProfileRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final PublisherProfileRepository publisherProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    public AuthService(AccountRepository accountRepository,
                       PublisherProfileRepository publisherProfileRepository,
                       UserProfileRepository userProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.publisherProfileRepository = publisherProfileRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // This annotation ensures the entire method is one transaction.
    // If any part fails, everything is rolled back.
    @Transactional
    public Account registerNewUser(String name, String email, String password, String role) {
        logger.info("Registration attempt for email: {}", email); // INFO level for general operations
        if (accountRepository.findByEmail(email).isPresent()) {
            logger.warn("Registration failed: Email already exists for {}", email); // WARN for potential problems
            throw new EmailAlreadyExistsException("An account with the email '" + email + "' already exists.");
        }

        Account account = new Account();
        account.setName(name);
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));

        if (role != null && role.equalsIgnoreCase("PUBLISHER")) {
            account.setRole(Account.Role.PUBLISHER);
            // The save will be handled by the transaction at the end.
            Account savedAccount = accountRepository.save(account);

            PublisherProfile profile = new PublisherProfile();
            profile.setAccount(savedAccount);
            profile.setPoints(0);
            publisherProfileRepository.save(profile);
            return savedAccount;
        } else { // Default to USER
            account.setRole(Account.Role.USER);
            Account savedAccount = accountRepository.save(account);

            UserProfile profile = new UserProfile();
            profile.setAccount(savedAccount);
            profile.setPoints(0);
            userProfileRepository.save(profile);
            logger.info("Successfully registered and created profile for email: {}", email);

            return savedAccount;
        }
    }
}