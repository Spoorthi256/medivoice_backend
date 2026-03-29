package com.mediVoice.backend.dto;

import java.time.LocalDateTime;

public class QueryHistoryResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long patientId;
    private String patientName;
    private String queryType;
    private String queryText;
    private String responseText;
    private String severityLevel;
    private LocalDateTime createdAt;

    // Constructors
    public QueryHistoryResponse() {}

    public QueryHistoryResponse(Long id, Long userId, String username, Long patientId, String patientName,
                               String queryType, String queryText, String responseText, String severityLevel,
                               LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.patientId = patientId;
        this.patientName = patientName;
        this.queryType = queryType;
        this.queryText = queryText;
        this.responseText = responseText;
        this.severityLevel = severityLevel;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }

    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }

    public String getResponseText() { return responseText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}