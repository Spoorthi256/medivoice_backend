package com.mediVoice.backend.service;

import com.mediVoice.backend.exception.DuplicateEmailException;
import com.mediVoice.backend.exception.InvalidCredentialsException;
import com.mediVoice.backend.exception.UserNotFoundException;
import com.mediVoice.backend.model.User;
import com.mediVoice.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existing -> {
                    throw new DuplicateEmailException("A user with email '" + user.getEmail() + "' already exists.");
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
