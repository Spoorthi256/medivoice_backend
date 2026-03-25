package com.mediVoice.backend.dto;

import java.util.HashMap;
import java.util.Map;

public class DashboardResponse {

    private String role;
    private Map<String, Object> data = new HashMap<>();

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}
