package com.university.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private String id;
    private String studentId;
    private String description;
    private String status;
    private LocalDateTime submissionDate;
    private String resolution;

    public Complaint(String studentId, String description) {
        this.id = generateId(); // You need to implement this method
        this.studentId = studentId;
        this.description = description;
        this.status = "Pending";
        this.submissionDate = LocalDateTime.now();
    }
    private String generateId() {
        // Implement a method to generate a unique ID
        return "COMP" + System.currentTimeMillis();
    }
    
    public Complaint(String id, String studentId, String description) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = "Pending";
        this.submissionDate = LocalDateTime.now();
        this.resolution = "";
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public String getResolution() {
        return resolution;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    // Additional methods
    public boolean isPending() {
        return "Pending".equals(status);
    }

    public boolean isResolved() {
        return "Resolved".equals(status);
    }

    public void resolve(String resolution) {
        this.status = "Resolved";
        this.resolution = resolution;
    }

    public String getFormattedSubmissionDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return submissionDate.format(formatter);
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", submissionDate=" + getFormattedSubmissionDate() +
                ", resolution='" + resolution + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complaint)) return false;
        Complaint complaint = (Complaint) o;
        return getId().equals(complaint.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}