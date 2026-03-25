package com.mediVoice.backend.controller;

import com.mediVoice.backend.dto.MedicalHistoryRequest;
import com.mediVoice.backend.dto.MedicalHistoryResponse;
import com.mediVoice.backend.service.MedicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<MedicalHistoryResponse> create(@Valid @RequestBody MedicalHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalHistoryService.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<MedicalHistoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalHistoryService.getById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<List<MedicalHistoryResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalHistoryService.getByPatientId(patientId));
    }
}
