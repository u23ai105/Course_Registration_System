package com.university.services;

import com.university.models.Administrator;
import com.university.models.Complaint;
import com.university.models.Student;
import com.university.models.Course;
import com.university.models.Professor;
import com.university.repositories.AdministratorRepository;
import com.university.repositories.StudentRepository;
import com.university.repositories.CourseRepository;
import com.university.repositories.ProfessorRepository;
import com.university.repositories.ComplaintRepository;

import java.util.List;
import java.util.Optional;

public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final ComplaintRepository complaintRepository;

    public AdministratorServiceImpl(AdministratorRepository administratorRepository, 
                                     StudentRepository studentRepository, 
                                     CourseRepository courseRepository, 
                                     ProfessorRepository professorRepository, 
                                     ComplaintRepository complaintRepository) {
        this.administratorRepository = administratorRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public void addStudent(Student student) { // Added @Override annotation
        studentRepository.save(student);
    }

    @Override
    public void removeStudent(String studentId) {
        studentRepository.delete(studentId);
    }

    @Override
    public List<Student> viewAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void removeCourse(String courseCode) {
        courseRepository.delete(courseCode);
    }

    @Override
    public List<Course> viewAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void addProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    @Override
    public void removeProfessor(String professorId) {
        professorRepository.delete(professorId);
    }

    @Override
    public List<Professor> viewAllProfessors() {
        return professorRepository.findAll();
    }

    @Override
    public void handleComplaint(String complaintId) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isPresent()) {
            Complaint complaint = complaintOpt.get();
            // Here you can implement logic to take action on the complaint
            // For example, you might want to change its status or log it
            System.out.println("Handling complaint: " + complaint.getDescription());
            // You can also update the status of the complaint if needed
        } else {
            System.out.println("Complaint not found.");
        }
    }

    @Override
    public List<Complaint> viewAllComplaints() {
        return complaintRepository.findAll();
    }

    @Override
    public void updateStudent(Student student) {
        Optional<Student> existingStudentOpt = studentRepository.findById(student.getId());
        if (existingStudentOpt.isPresent()) {
            studentRepository.save(student); // Save the updated student record
            System.out.println("Student record updated successfully!");
        } else {
            System.out.println("Student not found.");
        }
    }

    @Override
    public void assignProfessorToCourse(String professorId, String courseCode) {
        Optional<Professor> professorOpt = professorRepository.findById(professorId);
        Optional<Course> courseOpt = courseRepository.findById(courseCode);
        
        if (professorOpt.isPresent() && courseOpt.isPresent()) {
            Professor professor = professorOpt.get();
            Course course = courseOpt.get();
            course.setProfessorId(professorId); // Assuming Course has a method to set professor ID
            courseRepository.save(course); // Save the updated course
            System.out.println("Professor assigned to course successfully!");
        } else {
            System.out.println("Professor or Course not found.");
        }
    }

    @Override
    public List<Complaint> viewComplaints() {
        return complaintRepository.findAll(); // Return all complaints from the repository
    }

    @Override
    public void resolveComplaint(String complaintId, String resolution) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isPresent()) {
            Complaint complaint = complaintOpt.get();
            complaint.setStatus("Resolved"); // Update the status to resolved
            complaint.setResolution(resolution); // Assuming Complaint has a method to set resolution
            complaintRepository.save(complaint); // Save the updated complaint
            System.out.println("Complaint resolved successfully!");
        } else {
            System.out.println("Complaint not found.");
        }
    }

    @Override
    public Optional<Course> findByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode); // Assuming this method exists in CourseRepository
    }
    
    @Override
    public void updateCourse(Course course) {
        Optional<Course> existingCourseOpt = courseRepository.findByCourseCode(course.getCode());
        if (existingCourseOpt.isPresent()) {
            Course existingCourse = existingCourseOpt.get();
            // Update the existing course's details
            existingCourse.setTitle(course.getTitle());
            existingCourse.setCredits(course.getCredits());
            existingCourse.setTimings(course.getTimings());
            existingCourse.setLocation(course.getLocation());
            existingCourse.setEnrollmentLimit(course.getEnrollmentLimit());
            existingCourse.setDepartment(course.getDepartment());
            existingCourse.setSemester(course.getSemester()); // Ensure this method exists in Course class
            // Save the updated course
            courseRepository.save(existingCourse);
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }
    
//    @Override
//    public List<Student> viewAllStudents() {
//        return studentRepository.findAll(); // Assuming findAll() method exists in StudentRepository
//    }
    
    @Override
    public void deleteCourse(String courseCode) {
        Optional<Course> courseOpt = courseRepository.findByCourseCode(courseCode);
        if (courseOpt.isPresent()) {
            courseRepository.delete(courseCode); // Assuming delete method exists in CourseRepository
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }
    // Additional methods can be added as needed
}