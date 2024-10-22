package com.university.services;

import com.university.models.Student;
import com.university.models.Course;
import com.university.models.Complaint;
import com.university.repositories.StudentRepository;
import com.university.repositories.CourseRepository;
import com.university.repositories.ComplaintRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private ComplaintRepository complaintRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, ComplaintRepository complaintRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public List<Course> viewAvailableCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> !course.isEnrollmentFull())
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> viewAvailableCoursesForSemester(int semester) {
        // Fetch all courses and filter by semester and enrollment status
        return courseRepository.findAll().stream()
                .filter(course -> course.getSemester() == semester) // Check if the course is for the specified semester
                .filter(course -> !course.isEnrollmentFull()) // Check if the course is not full
                .collect(Collectors.toList());
    }
    @Override
    public boolean enrollInCourse(Student student, String courseCode, int credits) {
        Course course = courseRepository.findByCourseCode(courseCode).orElse(null);
        if (course == null || course.isEnrollmentFull()) {
            //System.out.println(1);
            return false;
        }
        if (student.getEnrolledCourses().contains(courseCode)) {
            //System.out.println(2);
            return false; // Already enrolled
        }
        
        for(String preq : course.getPrerequisites()) {
        	if(!student.hasCompleted(preq)) {
        		return false;
        	}
        }
        
        // Attempt to enroll the student in the course
        if (student.enrollCourse(courseCode, credits)) {
            course.enrollStudent(student.getId());
            studentRepository.save(student); // Save updated student with total credits
            courseRepository.save(course);
            return true;
        } else {
            System.out.println("Cannot enroll: Total credits for the semester would exceed 20.");
            return false; // Enrollment failed due to credit limit
        }
    }

    @Override
    public List<Course> viewEnrolledCourses(Student student) {
        return student.getEnrolledCourses().stream()
                .map(courseCode -> courseRepository.findByCourseCode(courseCode).orElse(null))
                .filter(course -> course != null)
                .collect(Collectors.toList());
    }

    @Override
    public boolean dropCourse(Student student, String courseCode,int credits) {
        if (!student.getEnrolledCourses().contains(courseCode)) {
            return false; // Not enrolled in this course
        }
        Course course = courseRepository.findByCourseCode(courseCode).orElse(null);
        if (course == null) {
            return false;
        }
        student.dropCourse(courseCode,credits);
        course.unenrollStudent(student.getId());
        studentRepository.save(student);
        courseRepository.save(course);
        return true;
    }

    @Override
    public List<String> viewGrades(Student student) {
        // Assuming the Student class has a method getGrades() that returns a Map<String, String>
        // where the key is the course code and the value is the grade
        return student.getGrades().entrySet().stream()
                .map(entry -> {
                    Course course = courseRepository.findById(entry.getKey()).orElse(null);
                    String courseName = course != null ? course.getTitle() : "Unknown Course";
                    return courseName + ": " + entry.getValue();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void submitComplaint(Student student, String complaintText) {
        Complaint complaint = new Complaint(student.getId(), complaintText);
        complaintRepository.save(complaint);
    }
    
    public List<Complaint> viewComplaint(Student student){
    	return complaintRepository.findByStudentId(student.getId());
    }

    // Additional helper methods

    public Optional<Student> getByStudentId(String studentId) {
        return Optional.ofNullable(studentRepository.findById(studentId).orElse(null));
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}