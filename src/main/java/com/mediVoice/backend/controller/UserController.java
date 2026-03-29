package com.mediVoice.backend.controller;

import com.mediVoice.backend.dto.AuthResponse;
import com.mediVoice.backend.dto.LoginRequest;
import com.mediVoice.backend.dto.RegisterRequest;
import com.mediVoice.backend.dto.UserResponse;
import com.mediVoice.backend.model.User;
import com.mediVoice.backend.security.JwtUtil;
import com.mediVoice.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole(),
                request.getSpecialization()
        );
        User saved = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toUserResponse(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        User user = userService.loginUser(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        return ResponseEntity.ok(new AuthResponse(token, toUserResponse(user)));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getSpecialization()
        );
    }
}
