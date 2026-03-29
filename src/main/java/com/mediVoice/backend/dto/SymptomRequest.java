package com.mediVoice.backend.dto;

public class SymptomRequest {
    private String symptom;
    private String duration;
    private String intensity;

    // Constructors
    public SymptomRequest() {}

    public SymptomRequest(String symptom, String duration, String intensity) {
        this.symptom = symptom;
        this.duration = duration;
        this.intensity = intensity;
    }

    // Getters and Setters
    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getIntensity() { return intensity; }
    public void setIntensity(String intensity) { this.intensity = intensity; }
}