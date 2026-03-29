package com.mediVoice.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/health-tips")
public class HealthTipsController {

    @GetMapping
    public ResponseEntity<List<String>> getHealthTips() {
        List<String> tips = Arrays.asList(
                "Stay hydrated by drinking at least 8 glasses of water daily.",
                "Aim for 7-9 hours of quality sleep each night.",
                "Include a variety of fruits and vegetables in your diet.",
                "Exercise regularly - aim for at least 30 minutes of moderate activity daily.",
                "Practice good hand hygiene to prevent illness.",
                "Schedule regular health check-ups with your healthcare provider.",
                "Manage stress through relaxation techniques like meditation.",
                "Maintain a healthy weight for your body type.",
                "Limit processed foods and sugary drinks.",
                "Get vaccinated according to recommended schedules."
        );

        return ResponseEntity.ok(tips);
    }
}