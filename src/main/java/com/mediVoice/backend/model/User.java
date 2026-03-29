package com.mediVoice.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 1)
    @Column(nullable = false, length = 255)
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "admin|doctor|receptionist", message = "Role must be 'admin', 'doctor' or 'receptionist'")
    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "specialization", length = 100)
    private String specialization;

    public User() {
    }

    public User(String username, String email, String password, String role, String specialization) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.specialization = specialization;
    }

    public User(String username, String email, String password, String role) {
        this(username, email, password, role, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
