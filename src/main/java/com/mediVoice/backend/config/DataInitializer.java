package com.mediVoice.backend.config;

import com.mediVoice.backend.model.User;
import com.mediVoice.backend.repository.UserRepository;
import com.mediVoice.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seed-demo-data", havingValue = "true")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final UserService userService;

    public DataInitializer(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin@medivoice.com", "Administrator", "admin123", "admin");
        createUserIfNotExists("doctor@medivoice.com", "Doctor", "doctor123", "doctor");
        createUserIfNotExists("receptionist@medivoice.com", "Receptionist", "receptionist123", "receptionist");
    }

    private void createUserIfNotExists(String email, String username, String password, String role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User(username, email, password, role);
            userService.registerUser(user);
            log.info("Initialized user {} ({})", email, role);
        }
    }
}
