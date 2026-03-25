package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.MedicalHistoryRequest;
import com.mediVoice.backend.dto.MedicalHistoryResponse;
import com.mediVoice.backend.exception.ResourceNotFoundException;
import com.mediVoice.backend.model.MedicalHistory;
import com.mediVoice.backend.model.Patient;
import com.mediVoice.backend.model.Prescription;
import com.mediVoice.backend.repository.MedicalHistoryRepository;
import com.mediVoice.backend.repository.PatientRepository;
import com.mediVoice.backend.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;

    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository, PatientRepository patientRepository, PrescriptionRepository prescriptionRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Transactional
    public MedicalHistoryResponse create(MedicalHistoryRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        MedicalHistory m = new MedicalHistory();
        m.setPatient(patient);
        m.setDiagnosis(request.getDiagnosis());
        m.setDiagnosisDate(request.getDiagnosisDate());
        m.setNotes(request.getNotes());
        if (request.getPrescriptionId() != null) {
            Prescription pr = prescriptionRepository.findById(request.getPrescriptionId()).orElse(null);
            m.setPrescription(pr);
        }
        return toResponse(medicalHistoryRepository.save(m));
    }

    public MedicalHistoryResponse getById(Long id) {
        MedicalHistory m = medicalHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medical history not found: " + id));
        return toResponse(m);
    }

    public List<MedicalHistoryResponse> getByPatientId(Long patientId) {
        return medicalHistoryRepository.findByPatientIdOrderByDiagnosisDateDesc(patientId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private MedicalHistoryResponse toResponse(MedicalHistory m) {
        MedicalHistoryResponse r = new MedicalHistoryResponse();
        r.setId(m.getId());
        r.setPatientId(m.getPatient().getId());
        r.setDiagnosis(m.getDiagnosis());
        r.setDiagnosisDate(m.getDiagnosisDate());
        r.setNotes(m.getNotes());
        if (m.getPrescription() != null) r.setPrescriptionId(m.getPrescription().getId());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}
