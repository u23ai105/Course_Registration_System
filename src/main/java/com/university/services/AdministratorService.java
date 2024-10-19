package com.university.services;

import com.university.models.Course;
import com.university.models.Student;
import com.university.models.Professor;
import com.university.models.Complaint;
import java.util.List;
import java.util.Optional;

public interface AdministratorService {
    void addCourse(Course course);
    void removeCourse(String courseCode);
    List<Course> viewAllCourses();
    void updateStudent(Student student);
    List<Student> viewAllStudents();
    void assignProfessorToCourse(String professorId, String courseCode);
    List<Complaint> viewComplaints();
    void resolveComplaint(String complaintId, String resolution);
    void addStudent(Student student); // Ensure this matches exactly
    void removeStudent(String studentId);
    void addProfessor(Professor professor);
    void removeProfessor(String professorId);
    List<Professor> viewAllProfessors();
    void handleComplaint(String complaintId);
    List<Complaint> viewAllComplaints();
    Optional<Course> findByCourseCode(String courseCode);
    void updateCourse(Course course);
    void deleteCourse(String courseCode);
}