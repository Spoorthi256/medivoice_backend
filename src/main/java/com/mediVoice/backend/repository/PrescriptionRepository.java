package com.mediVoice.backend.repository;

import com.mediVoice.backend.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientIdOrderByPrescriptionDateDesc(Long patientId);
}
