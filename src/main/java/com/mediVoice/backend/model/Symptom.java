package com.mediVoice.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "symptoms")
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "query_id", nullable = false)
    private QueryHistory query;

    @Column(nullable = false)
    private String symptom;

    private String duration;

    private String intensity;

    // Constructors
    public Symptom() {}

    public Symptom(QueryHistory query, String symptom, String duration, String intensity) {
        this.query = query;
        this.symptom = symptom;
        this.duration = duration;
        this.intensity = intensity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public QueryHistory getQuery() { return query; }
    public void setQuery(QueryHistory query) { this.query = query; }

    public String getSymptom() { return symptom; }
    public void setSymptom(String symptom) { this.symptom = symptom; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getIntensity() { return intensity; }
    public void setIntensity(String intensity) { this.intensity = intensity; }
}