package com.university.repositories;

import com.university.models.Course;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends Repository<Course> {
	
    
    // Inherited methods from Repository<Course>
    @Override
    List<Course> findAll();

    @Override
    Optional<Course> findById(String id);

    @Override
    void save(Course entity);

    @Override
    void delete(String id);

    // Course-specific methods
    /**
     * Finds all courses taught by a specific professor.
     *
     * @param professorId The ID of the professor
     * @return A list of courses taught by the professor
     */
    List<Course> findByProfessorId(String professorId);
    
    /**
     * Finds all courses that are available for enrollment.
     *
     * @return A list of available courses
     */
    List<Course> findAvailableCourses();
    
    /**
     * Checks if a student is enrolled in a specific course.
     *
     * @param courseId The ID of the course
     * @param studentId The ID of the student
     * @return true if the student is enrolled, false otherwise
     */
    boolean isStudentEnrolled(String courseId, String studentId);
    
    /**
     * Enrolls a student in a course.
     *
     * @param courseId The ID of the course
     * @param studentId The ID of the student
     */
    void enrollStudent(String courseId, String studentId);
    
    /**
     * Unenrolls a student from a course.
     *
     * @param courseId The ID of the course
     * @param studentId The ID of the student
     */
    void unenrollStudent(String courseId, String studentId);
    
    /**
     * Finds all courses a student is enrolled in.
     *
     * @param studentId The ID of the student
     * @return A list of courses the student is enrolled in
     */
    List<Course> findCoursesByStudentId(String studentId);

    /**
     * Finds a course by its code.
     *
     * @param courseCode The code of the course
     * @return An Optional containing the course if found, or an empty Optional if not found
     */
    Optional<Course> findByCourseCode(String courseCode);

    /**
     * Updates the details of an existing course.
     *
     * @param course The Course object with updated information
     */
    void updateCourse(Course course);

    /**
     * Finds all courses in a specific department.
     *
     * @param department The name of the department
     * @return A list of courses in the specified department
     */
    List<Course> findCoursesByDepartment(String department);

    /**
     * Finds all courses with available seats.
     *
     * @return A list of courses that are not full
     */
    List<Course> findCoursesWithAvailableSeats();
}