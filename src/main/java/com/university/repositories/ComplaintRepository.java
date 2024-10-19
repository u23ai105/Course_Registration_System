package com.university.repositories;

import com.university.models.Complaint;
import java.util.List;

public interface ComplaintRepository extends Repository<Complaint> {
    
    // Inherited methods from Repository<Complaint>:
    // List<Complaint> findAll();
    // Optional<Complaint> findById(String id);
    // void save(Complaint entity);
    // void delete(String id);

    // Complaint-specific methods
    List<Complaint> findByStudentId(String studentId);
    
    List<Complaint> findByStatus(String status);
    
    void updateStatus(String complaintId, String newStatus);
    
    List<Complaint> findUnresolvedComplaints();
}