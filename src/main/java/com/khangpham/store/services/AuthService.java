package com.khangpham.store.services;

import com.khangpham.store.entities.User;
import com.khangpham.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        // Get userId from authentication context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        // Fetch user from database by userId
        return userRepository.findById(userId).orElse(null);

    }
}
