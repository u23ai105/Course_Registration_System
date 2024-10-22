package com.university.services;

import com.university.models.Student;
import com.university.models.Complaint;
import com.university.models.Course;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    
    /**
     * Retrieves a list of all available courses that are not full.
     *
     * @return A list of available Course objects
     */
    List<Course> viewAvailableCourses();

    /**
     * Enrolls a student in a specific course.
     *
     * @param student The Student object to be enrolled
     * @param courseCode The code of the course to enroll in
     * @return true if enrollment was successful, false otherwise
     */
    boolean enrollInCourse(Student student, String courseCode,int credits);

    /**
     * Retrieves a list of courses that the student is currently enrolled in.
     *
     * @param student The Student object whose enrolled courses are to be retrieved
     * @return A list of Course objects that the student is enrolled in
     */
    List<Course> viewEnrolledCourses(Student student);

    /**
     * Drops a student from a specific course.
     *
     * @param student The Student object to be dropped from the course
     * @param courseCode The code of the course to drop
     * @return true if the course was successfully dropped, false otherwise
     */
    boolean dropCourse(Student student, String courseCode,int semester);

    /**
     * Retrieves a list of grades for the student's courses.
     *
     * @param student The Student object whose grades are to be retrieved
     * @return A list of strings representing the student's grades
     */
    List<String> viewGrades(Student student);

    /**
     * Submits a complaint on behalf of a student.
     *
     * @param student The Student object submitting the complaint
     * @param complaintText The text of the complaint
     */
    void submitComplaint(Student student, String complaintText);
    
    List<Complaint> viewComplaint(Student student);

    /**
     * Retrieves a student by their ID.
     *
     * @param studentId The ID of the student to retrieve
     * @return The Student object if found, null otherwise
     */
    Optional<Student> getByStudentId(String studentId);

    /**
     * Updates the information of a student.
     *
     * @param student The Student object with updated information
     */
    void updateStudent(Student student);

    
    /**
     * Retrieves a list of all students in the system.
     *
     * @return A list of all Student objects
     */
    List<Student> getAllStudents();

    List<Course> viewAvailableCoursesForSemester(int semester);
    
}