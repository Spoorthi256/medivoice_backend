package com.mediVoice.backend.dto;

public class SymptomResponse {
    private Long id;
    private String symptom;
    private String duration;
    private String intensity;

    // Constructors
    public SymptomResponse() {}

    public SymptomResponse(Long id, String symptom, String duration, String intensity) {
        this.id = id;
        this.symptom = symptom;
        this.duration = duration;
        this.intensity = intensity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getIntensity() { return intensity; }
    public void setIntensity(String intensity) { this.intensity = intensity; }
}