package com.mediVoice.backend.controller;

import com.mediVoice.backend.dto.QueryHistoryRequest;
import com.mediVoice.backend.dto.QueryHistoryResponse;
import com.mediVoice.backend.service.SymptomAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptom-analysis")
public class SymptomAnalysisController {

    @Autowired
    private SymptomAnalysisService symptomAnalysisService;

    @Autowired
    private com.mediVoice.backend.repository.UserRepository userRepository;

    @PostMapping("/analyze")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<QueryHistoryResponse> analyzeSymptoms(
            @RequestBody QueryHistoryRequest request,
            Authentication authentication) {

        Long userId = getUserIdFromAuthentication(authentication);
        QueryHistoryResponse response = symptomAnalysisService.analyzeSymptoms(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<List<QueryHistoryResponse>> getQueryHistory(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<QueryHistoryResponse> history = symptomAnalysisService.getQueryHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/patient/{patientId}/history")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<QueryHistoryResponse>> getPatientQueryHistory(@PathVariable Long patientId) {
        List<QueryHistoryResponse> history = symptomAnalysisService.getPatientQueryHistory(patientId);
        return ResponseEntity.ok(history);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email))
                .getId();
    }
}