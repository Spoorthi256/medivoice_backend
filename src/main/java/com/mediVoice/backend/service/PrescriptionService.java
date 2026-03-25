package com.mediVoice.backend.service;

import com.mediVoice.backend.dto.*;
import com.mediVoice.backend.exception.ResourceNotFoundException;
import com.mediVoice.backend.model.*;
import com.mediVoice.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PatientRepository patientRepository, UserRepository userRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PrescriptionResponse create(PrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        User doctor = userRepository.findById(request.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        Prescription p = new Prescription();
        p.setPatient(patient);
        p.setDoctor(doctor);
        p.setPrescriptionDate(request.getPrescriptionDate());
        p.setNotes(request.getNotes());
        for (PrescriptionItemRequest itemReq : request.getItems()) {
            PrescriptionItem item = new PrescriptionItem();
            item.setPrescription(p);
            item.setMedicineName(itemReq.getMedicineName());
            item.setDosage(itemReq.getDosage());
            item.setInstructions(itemReq.getInstructions());
            p.getItems().add(item);
        }
        return toResponse(prescriptionRepository.save(p));
    }

    public PrescriptionResponse getById(Long id) {
        Prescription p = prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription not found: " + id));
        return toResponse(p);
    }

    public List<PrescriptionResponse> getByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientIdOrderByPrescriptionDateDesc(patientId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private PrescriptionResponse toResponse(Prescription p) {
        PrescriptionResponse r = new PrescriptionResponse();
        r.setId(p.getId());
        r.setPatientId(p.getPatient().getId());
        r.setDoctorId(p.getDoctor().getId());
        r.setPrescriptionDate(p.getPrescriptionDate());
        r.setNotes(p.getNotes());
        r.setCreatedAt(p.getCreatedAt());
        for (PrescriptionItem i : p.getItems()) {
            PrescriptionItemResponse ir = new PrescriptionItemResponse();
            ir.setId(i.getId());
            ir.setMedicineName(i.getMedicineName());
            ir.setDosage(i.getDosage());
            ir.setInstructions(i.getInstructions());
            r.getItems().add(ir);
        }
        return r;
    }
}
