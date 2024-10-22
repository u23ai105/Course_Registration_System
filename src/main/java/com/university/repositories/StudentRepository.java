package com.university.repositories;

import com.university.models.Complaint;
import com.university.repositories.ComplaintRepositoryImpl;
import com.university.models.Student;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentRepository extends JsonRepository<Student> {
    
    public StudentRepository() {
        super("src/main/resources/students.json");
    }

    @Override
    protected Student fromJson(JSONObject jsonObject) {
        Student student = new Student(
            jsonObject.getString("id"),
            jsonObject.getString("name"),
            jsonObject.getString("email"),
            jsonObject.getString("password")
        );
        student.setCurrentSemester(jsonObject.getInt("currentSemester"));
        student.settotalCredits(jsonObject.getInt("totalCredits"));
        JSONArray enrolledCoursesArray = jsonObject.getJSONArray("enrolledCourses");
        for (int i = 0; i < enrolledCoursesArray.length(); i++) {
            student.enrollCourse(enrolledCoursesArray.getString(i));
        }
        
        JSONArray completedCoursesArray = jsonObject.getJSONArray("completedCourses");
        for (int i = 0; i < completedCoursesArray.length(); i++) {
            student.completeCourse(completedCoursesArray.getString(i));
        }
        
        return student;
    }

    @Override
    protected JSONObject toJson(Student student) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", student.getId());
        jsonObject.put("name", student.getName());
        jsonObject.put("email", student.getEmail());
        jsonObject.put("password", student.getPassword());
        jsonObject.put("currentSemester", student.getCurrentSemester());
        jsonObject.put("totalCredits", student.gettotalCredits());
        jsonObject.put("enrolledCourses", new JSONArray(student.getEnrolledCourses()));
        jsonObject.put("completedCourses", new JSONArray(student.getCompletedCourses()));
        return jsonObject;
    }

//    @Override
//    public Optional<Student> findById(String id) {
//        return findAll().stream()
//                .filter(student -> student.getId().equalsIgnoreCase(id)) // Match the ID exactly
//                .findFirst(); // Return the first matching student wrapped in an Optional
//    }
    
    @Override
    public Optional<Student> findById(String id) {
        return findAll().stream()
                .filter(student -> student.getId().equalsIgnoreCase(id)) // Match the ID exactly
                .findFirst(); // Return the first matching student wrapped in an Optional
    }
    
    public List<Student> findByName(String name) {
        return findAll().stream()
                .filter(student -> student.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Student> findByCurrentSemester(int semester) {
        return findAll().stream()
                .filter(student -> student.getCurrentSemester() == semester)
                .collect(Collectors.toList());
    }

    public List<Student> findByEnrolledCourse(String courseCode) {
        return findAll().stream()
                .filter(student -> student.getEnrolledCourses().contains(courseCode))
                .collect(Collectors.toList());
    }

    public List<Student> findByCompletedCourse(String courseCode) {
        return findAll().stream()
                .filter(student -> student.getCompletedCourses().contains(courseCode))
                .collect(Collectors.toList());
    }

    public boolean existsByEmail(String email) {
        return findAll().stream()
                .anyMatch(student -> student.getEmail().equals(email));
    }

    public void updateGrade(String studentId, String courseCode, String grade) {
        findById(studentId).ifPresent(student -> {
            student.completeCourse(courseCode);
            // Assuming there's a method to set grade for a course
            // student.setGradeForCourse(courseCode, grade);
            save(student);
        });
    }

    public void enrollStudentInCourse(String studentId, String courseCode, int credits) {
        findById(studentId).ifPresent(student -> {
            if (student.enrollCourse(courseCode, credits)) { // Check if enrollment is successful
                save(student); // Save updated student with total credits
            } else {
                System.out.println("Enrollment failed: Total credits would exceed limit.");
            }
        });
    }
    
//    public void enrollStudentInCourse(String studentId, String courseCode) {
//    	int credits=0;
//        findById(studentId).ifPresent(student -> {
//            student.enrollCourse(courseCode,credits);
//            save(student);
//        });
//    }

    public void unenrollStudentFromCourse(String studentId, String courseCode,int credits) {
        findById(studentId).ifPresent(student -> {
            student.dropCourse(courseCode,credits);
            save(student);
        });
    }

    public void advanceStudentSemester(String studentId) {
        findById(studentId).ifPresent(student -> {
            student.advanceToNextSemester();
            save(student);
        });
    }

    public List<Student> findStudentsEligibleForGraduation() {
        // This is a placeholder implementation. You should define your own graduation criteria.
        return findAll().stream()
                .filter(student -> student.getCurrentSemester() > 8 && student.getCompletedCourses().size() >= 40)
                .collect(Collectors.toList());
    }
    
//    public List<Complaint> displaycomplaints(String studentId) {
//    	ComplaintRepository c;
//    	return ComplaintRepository.findByStudentId(studentId);
//    }
}