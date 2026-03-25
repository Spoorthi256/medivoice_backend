package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.PatientRequest;
import com.mediVoice.backend.dto.PatientResponse;
import com.mediVoice.backend.exception.ResourceNotFoundException;
import com.mediVoice.backend.model.Patient;
import com.mediVoice.backend.model.User;
import com.mediVoice.backend.repository.PatientRepository;
import com.mediVoice.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PatientResponse create(PatientRequest request, Long createdById) {
        User createdBy = userRepository.findById(createdById).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Patient p = new Patient();
        mapRequestToEntity(request, p);
        p.setCreatedBy(createdBy);
        return toResponse(patientRepository.save(p));
    }

    @Transactional
    public PatientResponse update(Long id, PatientRequest request) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        mapRequestToEntity(request, p);
        return toResponse(patientRepository.save(p));
    }

    public PatientResponse getById(Long id) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        return toResponse(p);
    }

    public List<PatientResponse> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return patientRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
        }
        return patientRepository.search(query.trim()).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<PatientResponse> getAll() {
        return patientRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private void mapRequestToEntity(PatientRequest req, Patient p) {
        p.setFirstName(req.getFirstName());
        p.setLastName(req.getLastName());
        p.setDateOfBirth(req.getDateOfBirth());
        p.setPhone(req.getPhone());
        p.setEmail(req.getEmail());
        p.setGender(req.getGender());
        p.setAllergies(req.getAllergies());
        p.setMedicalHistory(req.getMedicalHistory());
        p.setAddress(req.getAddress());
    }

    private PatientResponse toResponse(Patient p) {
        PatientResponse r = new PatientResponse();
        r.setId(p.getId());
        r.setFirstName(p.getFirstName());
        r.setLastName(p.getLastName());
        r.setDateOfBirth(p.getDateOfBirth());
        r.setPhone(p.getPhone());
        r.setEmail(p.getEmail());
        r.setGender(p.getGender());
        r.setAllergies(p.getAllergies());
        r.setMedicalHistory(p.getMedicalHistory());
        r.setAddress(p.getAddress());
        r.setCreatedAt(p.getCreatedAt());
        if (p.getCreatedBy() != null) r.setCreatedById(p.getCreatedBy().getId());
        return r;
    }
}
