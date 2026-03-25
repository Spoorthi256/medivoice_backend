package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.VoiceNoteRequest;
import com.mediVoice.backend.dto.VoiceNoteResponse;
import com.mediVoice.backend.exception.ResourceNotFoundException;
import com.mediVoice.backend.model.*;
import com.mediVoice.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoiceNoteService {

    private final VoiceNoteRepository voiceNoteRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public VoiceNoteService(VoiceNoteRepository voiceNoteRepository, PatientRepository patientRepository,
                            UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.voiceNoteRepository = voiceNoteRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public VoiceNoteResponse create(VoiceNoteRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        User doctor = userRepository.findById(request.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        VoiceNote v = new VoiceNote();
        v.setPatient(patient);
        v.setDoctor(doctor);
        v.setTranscript(request.getTranscript());
        v.setAudioUrl(request.getAudioUrl());
        if (request.getAppointmentId() != null) {
            Appointment app = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
            v.setAppointment(app);
        }
        return toResponse(voiceNoteRepository.save(v));
    }

    public VoiceNoteResponse getById(Long id) {
        VoiceNote v = voiceNoteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Voice note not found: " + id));
        return toResponse(v);
    }

    public List<VoiceNoteResponse> getByPatientId(Long patientId) {
        return voiceNoteRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private VoiceNoteResponse toResponse(VoiceNote v) {
        VoiceNoteResponse r = new VoiceNoteResponse();
        r.setId(v.getId());
        r.setPatientId(v.getPatient().getId());
        r.setDoctorId(v.getDoctor().getId());
        if (v.getAppointment() != null) r.setAppointmentId(v.getAppointment().getId());
        r.setTranscript(v.getTranscript());
        r.setAudioUrl(v.getAudioUrl());
        r.setCreatedAt(v.getCreatedAt());
        return r;
    }
}
