package com.university.services;

import com.university.models.Professor;
import com.university.models.Course;
import com.university.models.Student;
import java.util.List;
import java.util.Optional;

public interface ProfessorService {
    List<Course> viewAssignedCourses(Professor professor);
    List<Student> viewCourseRoster(String courseCode);
    void assignGrade(String studentId, String courseCode, String grade);
	Optional<Professor> findProfessorById(String professorId);
	List<Professor> findAllProfessors();
	void addProfessor(Professor professor);
	void updateProfessor(Professor professor);
	void deleteProfessor(String professorId);
	List<Professor> findProfessorsByDepartment(String department);
	void assignCourse(String professorId, String courseCode);
	void removeCourseAssignment(String professorId, String courseCode);
	List<String> getCoursesTaughtByProfessor(String professorId);
}