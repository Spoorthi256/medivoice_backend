package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.QueryHistoryRequest;
import com.mediVoice.backend.dto.QueryHistoryResponse;
import com.mediVoice.backend.dto.SymptomRequest;
import com.mediVoice.backend.model.QueryHistory;
import com.mediVoice.backend.model.Symptom;
import com.mediVoice.backend.model.User;
import com.mediVoice.backend.model.Patient;
import com.mediVoice.backend.repository.QueryHistoryRepository;
import com.mediVoice.backend.repository.SymptomRepository;
import com.mediVoice.backend.repository.UserRepository;
import com.mediVoice.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SymptomAnalysisService {

    @Autowired
    private QueryHistoryRepository queryHistoryRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public QueryHistoryResponse analyzeSymptoms(Long userId, QueryHistoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = null;
        if (request.getPatientId() != null) {
            patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
        }

        // Simple symptom analysis logic
        String severityLevel = analyzeSeverity(request.getQueryText());

        // Generate structured response
        String responseText = generateResponse(request.getQueryText(), severityLevel);

        QueryHistory queryHistory = new QueryHistory(user, patient, request.getQueryType(),
                request.getQueryText(), responseText, severityLevel);

        queryHistory = queryHistoryRepository.save(queryHistory);

        return mapToResponse(queryHistory);
    }

    private String analyzeSeverity(String queryText) {
        String lowerQuery = queryText.toLowerCase();

        // Emergency keywords
        if (lowerQuery.contains("chest pain") || lowerQuery.contains("difficulty breathing") ||
            lowerQuery.contains("severe bleeding") || lowerQuery.contains("unconscious")) {
            return "emergency";
        }

        // High severity
        if (lowerQuery.contains("severe") || lowerQuery.contains("intense") ||
            lowerQuery.contains("can't move") || lowerQuery.contains("high fever")) {
            return "high";
        }

        // Medium severity
        if (lowerQuery.contains("moderate") || lowerQuery.contains("persistent") ||
            lowerQuery.contains("worsening")) {
            return "medium";
        }

        return "low";
    }

    private String generateResponse(String queryText, String severity) {
        StringBuilder response = new StringBuilder();

        switch (severity) {
            case "emergency":
                response.append("🚨 EMERGENCY ALERT: This appears to be a medical emergency. ");
                response.append("Please seek immediate medical attention or call emergency services. ");
                response.append("Do not wait - get help now!\n\n");
                break;
            case "high":
                response.append("⚠️ HIGH PRIORITY: Your symptoms require prompt medical attention. ");
                response.append("Please consult a healthcare professional as soon as possible.\n\n");
                break;
            case "medium":
                response.append("📋 MODERATE CONCERN: Monitor your symptoms closely. ");
                response.append("Consider consulting a healthcare professional if symptoms persist or worsen.\n\n");
                break;
            default:
                response.append("ℹ️ GENERAL ADVICE: For personalized medical advice, ");
                response.append("please consult with a qualified healthcare professional.\n\n");
        }

        response.append("Recommended actions:\n");
        response.append("• Rest and monitor your symptoms\n");
        response.append("• Stay hydrated\n");
        response.append("• Note any changes in symptoms\n");
        response.append("• Consult a healthcare provider for proper diagnosis\n\n");

        response.append("Remember: This is not a substitute for professional medical advice.");

        return response.toString();
    }

    public List<QueryHistoryResponse> getQueryHistory(Long userId) {
        List<QueryHistory> queries = queryHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return queries.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<QueryHistoryResponse> getPatientQueryHistory(Long patientId) {
        List<QueryHistory> queries = queryHistoryRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        return queries.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private QueryHistoryResponse mapToResponse(QueryHistory query) {
        String patientName = query.getPatient() != null ?
                query.getPatient().getFirstName() + " " + query.getPatient().getLastName() : null;

        return new QueryHistoryResponse(
                query.getId(),
                query.getUser().getId(),
                query.getUser().getUsername(),
                query.getPatient() != null ? query.getPatient().getId() : null,
                patientName,
                query.getQueryType(),
                query.getQueryText(),
                query.getResponseText(),
                query.getSeverityLevel(),
                query.getCreatedAt()
        );
    }
}