package com.mediVoice.backend.dto;

public class QueryHistoryRequest {
    private Long patientId;
    private String queryType;
    private String queryText;
    private String responseText;
    private String severityLevel;

    // Constructors
    public QueryHistoryRequest() {}

    public QueryHistoryRequest(Long patientId, String queryType, String queryText, String responseText, String severityLevel) {
        this.patientId = patientId;
        this.queryType = queryType;
        this.queryText = queryText;
        this.responseText = responseText;
        this.severityLevel = severityLevel;
    }

    // Getters and Setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }

    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }

    public String getResponseText() { return responseText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
}