package com.university.repositories;

import com.university.models.Professor;
import org.json.JSONObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfessorRepositoryImpl extends JsonRepository<Professor> implements ProfessorRepository {

    public ProfessorRepositoryImpl() {
        super("src/main/resources/professors.json");
    }

    @Override
    protected Professor fromJson(JSONObject jsonObject) {
        Professor professor = new Professor(
            jsonObject.getString("id"),
            jsonObject.getString("name"),
            jsonObject.getString("email"),
            jsonObject.getString("password"),
            jsonObject.getString("department")
        );
//        professor.setDepartment(jsonObject.getString("department"));
        return professor;
    }

    @Override
    protected JSONObject toJson(Professor professor) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", professor.getId());
        jsonObject.put("name", professor.getName());
        jsonObject.put("email", professor.getEmail());
        jsonObject.put("password", professor.getPassword());
        jsonObject.put("department", professor.getDepartment());
        return jsonObject;
    }

    
    public Optional<Professor> findByName(String name) {
        return findAll().stream()
                .filter(professor -> professor.getName().equals(name))
                .findFirst();
    }
    
    public Optional<Professor> findById(String id) {
        return findAll().stream()
                .filter(professor -> professor.getName().equals(id))
                .findFirst();
    }
    
    @Override
    public Optional<Professor> findByEmail(String email) {
        return findAll().stream()
                .filter(professor -> professor.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Professor> findByDepartment(String department) {
        return findAll().stream()
                .filter(professor -> professor.getDepartment().equals(department))
                .collect(Collectors.toList());
    }

    @Override
    public void assignCourse(String professorId, String courseCode) {
        findById(professorId).ifPresent(professor -> {
            professor.assignCourse(courseCode);
            save(professor);
        });
    }

    @Override
    public void removeCourseAssignment(String professorId, String courseCode) {
        findById(professorId).ifPresent(professor -> {
            professor.removeCourseAssignment(courseCode);
            save(professor);
        });
    }
}