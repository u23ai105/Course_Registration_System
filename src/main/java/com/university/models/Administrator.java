package com.university.models;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Administrator extends User {
	private String role;
    private Map<String, Course> courseCatalog;
    private Map<String, Student> studentRecords;
//    private Map<String, Student> professorRecords;
    private Map<String, Complaint> complaints;

    public Administrator(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.courseCatalog = new HashMap<>();
        this.studentRecords = new HashMap<>();
//        this.professorRecords = new HashMap<>();
        this.complaints = new HashMap<>();
        this.role = "Administrator";
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String getRole() {
        return role;
    }

    // 1. Manage Course Catalog
    public void addCourse(Course course) {
        courseCatalog.put(course.getCode(), course);
    }

    public void deleteCourse(String courseCode) {
        courseCatalog.remove(courseCode);
    }

    public Course getCourse(String courseCode) {
        return courseCatalog.get(courseCode);
    }

    public List<Course> getAllCourses() {
        return List.copyOf(courseCatalog.values());
    }

    // 2. Manage Student Records
    public void addStudent(Student student) {
        studentRecords.put(student.getId(), student);
    }

    public Student getStudent(String studentId) {
        return studentRecords.get(studentId);
    }

//    public void addProfessor(Professor professor) {
//        ProfessorRecords.put(professor.getId(), professor);
//    }
//
//    public Student getProfessor(String ProfessorId) {
//        return ProfessorRecords.get(ProfessorId);
//    }
    
    public void updateStudentGrade(String studentId, String courseCode, String grade) {
        Student student = studentRecords.get(studentId);
        if (student != null) {
            student.setGrade(courseCode, grade);
        }
    }

    public void updateStudentInfo(String studentId, String field, String value) {
        Student student = studentRecords.get(studentId);
        if (student != null) {
            switch (field.toLowerCase()) {
                case "name":
                    student.setName(value);
                    break;
                case "email":
                    student.setEmail(value);
                    break;
                // Add more fields as needed
            }
        }
    }

    // 3. Assign Professors to Courses
    public void assignProfessorToCourse(Professor professor, String courseCode) {
        Course course = courseCatalog.get(courseCode);
        if (course != null) {
            course.setProfessor(professor);
            professor.addCourse(courseCode);
        }
    }

    // 4. Handle Complaints
    public void addComplaint(Complaint complaint) {
        complaints.put(complaint.getId(), complaint);
    }

    public void updateComplaintStatus(String complaintId, String status) {
        Complaint complaint = complaints.get(complaintId);
        if (complaint != null) {
            complaint.setStatus(status);
        }
    }

    public List<Complaint> getAllComplaints() {
        return List.copyOf(complaints.values());
    }

    public List<Complaint> getPendingComplaints() {
        return complaints.values().stream()
                .filter(c -> "Pending".equals(c.getStatus()))
                .toList();
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}