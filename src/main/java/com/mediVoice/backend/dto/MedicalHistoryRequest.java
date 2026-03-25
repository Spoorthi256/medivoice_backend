package com.mediVoice.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class MedicalHistoryRequest {

    @NotNull
    private Long patientId;

    @NotBlank
    @Size(max = 300)
    private String diagnosis;

    @NotNull
    private LocalDate diagnosisDate;

    private String notes;

    private Long prescriptionId;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public LocalDate getDiagnosisDate() { return diagnosisDate; }
    public void setDiagnosisDate(LocalDate diagnosisDate) { this.diagnosisDate = diagnosisDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
}
