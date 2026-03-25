package com.mediVoice.backend.controller;

import com.mediVoice.backend.dto.VoiceNoteRequest;
import com.mediVoice.backend.dto.VoiceNoteResponse;
import com.mediVoice.backend.service.VoiceNoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voice-notes")
public class VoiceNoteController {

    private final VoiceNoteService voiceNoteService;

    public VoiceNoteController(VoiceNoteService voiceNoteService) {
        this.voiceNoteService = voiceNoteService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<VoiceNoteResponse> create(@Valid @RequestBody VoiceNoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voiceNoteService.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<VoiceNoteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voiceNoteService.getById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public ResponseEntity<List<VoiceNoteResponse>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(voiceNoteService.getByPatientId(patientId));
    }
}
