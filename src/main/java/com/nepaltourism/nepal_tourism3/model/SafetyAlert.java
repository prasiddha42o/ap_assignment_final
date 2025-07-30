package com.nepaltourism.nepal_tourism3.model;

import java.time.LocalDate;

public class SafetyAlert {
    private int id;
    private String title;
    private String description;
    private String alertType;
    private String severity;
    private String affectedAreas;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;

    public SafetyAlert(int id, String title, String description, String alertType,
                       String severity, String affectedAreas, LocalDate startDate,
                       LocalDate endDate, boolean isActive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.alertType = alertType;
        this.severity = severity;
        this.affectedAreas = affectedAreas;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getAffectedAreas() { return affectedAreas; }
    public void setAffectedAreas(String affectedAreas) { this.affectedAreas = affectedAreas; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}