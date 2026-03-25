package com.mediVoice.backend.repository;

import com.mediVoice.backend.model.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    List<MedicalHistory> findByPatientIdOrderByDiagnosisDateDesc(Long patientId);
}
