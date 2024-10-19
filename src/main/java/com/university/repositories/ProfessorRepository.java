package com.university.repositories;

import com.university.models.Professor;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends Repository<Professor> {
    Optional<Professor> findByEmail(String email);
    List<Professor> findByDepartment(String department);
    void assignCourse(String professorId, String courseCode);
    void removeCourseAssignment(String professorId, String courseCode);
}