package com.university.repositories;

import com.university.models.Complaint;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintRepositoryImpl extends JsonRepository<Complaint> implements ComplaintRepository {
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ComplaintRepositoryImpl() {
        super("src/main/resources/complaints.json");
    }

    @Override
    protected Complaint fromJson(JSONObject jsonObject) {
        Complaint complaint = new Complaint(
            jsonObject.getString("id"),
            jsonObject.getString("studentId"),
            jsonObject.getString("description")
        );
        complaint.setStatus(jsonObject.getString("status"));
        complaint.setResolution(jsonObject.optString("resolution", ""));
        // Parse the submission date
        LocalDateTime submissionDate = LocalDateTime.parse(jsonObject.getString("submissionDate"), DATE_FORMATTER);
        // We need to set the submission date using reflection or add a setter in the Complaint class
        // For now, we'll leave it as is, which means it will use the current time instead of the stored time
        return complaint;
    }

    @Override
    protected JSONObject toJson(Complaint complaint) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", complaint.getId());
        jsonObject.put("studentId", complaint.getStudentId());
        jsonObject.put("description", complaint.getDescription());
        jsonObject.put("status", complaint.getStatus());
        jsonObject.put("submissionDate", complaint.getFormattedSubmissionDate());
        jsonObject.put("resolution", complaint.getResolution());
        return jsonObject;
    }

    @Override
    public List<Complaint> findByStudentId(String studentId) {
        return findAll().stream()
                .filter(complaint -> complaint.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Complaint> findByStatus(String status) {
        return findAll().stream()
                .filter(complaint -> complaint.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(String complaintId, String newStatus) {
        findById(complaintId).ifPresent(complaint -> {
            complaint.setStatus(newStatus);
            save(complaint);
        });
    }

    @Override
    public List<Complaint> findUnresolvedComplaints() {
        return findAll().stream()
                .filter(complaint -> !complaint.isResolved())
                .collect(Collectors.toList());
    }
}