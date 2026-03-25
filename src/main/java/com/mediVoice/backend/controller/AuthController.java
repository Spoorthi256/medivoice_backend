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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        User saved = userService.registerUser(user);
        UserResponse res = new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.loginUser(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token, userResponse));
    }
}
