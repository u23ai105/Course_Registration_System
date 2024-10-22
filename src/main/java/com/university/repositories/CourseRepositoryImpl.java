package com.university.repositories;

import com.university.models.Course;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseRepositoryImpl extends JsonRepository<Course> implements CourseRepository {

    public CourseRepositoryImpl() {
        super("src/main/resources/courses.json");
    }

    @Override
    protected Course fromJson(JSONObject jsonObject) {
        Course course = new Course(
            jsonObject.getString("code"),
            jsonObject.getString("title"),
            jsonObject.getInt("credits"),
            jsonObject.getString("timings"),
            jsonObject.getString("location"),
            jsonObject.getInt("enrollmentLimit"),
            jsonObject.getString("department"),
            jsonObject.getInt("Semester")// Change "Semester" to "semester"
        );
        course.setProfessorId(jsonObject.optString("professorId", null));
        
        JSONArray prerequisites = jsonObject.optJSONArray("prerequisites");
        if (prerequisites != null) {
            for (int i = 0; i < prerequisites.length(); i++) {
                course.addPrerequisite(prerequisites.getString(i));
            }
        }
        
        JSONArray enrolledStudents = jsonObject.optJSONArray("enrolledStudents");
        if (enrolledStudents != null) {
            for (int i = 0; i < enrolledStudents.length(); i++) {
                course.enrollStudent(enrolledStudents.getString(i));
            }
        }
        return course;
    }

    @Override
    protected JSONObject toJson(Course course) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", course.getCode());
        jsonObject.put("title", course.getTitle());
        jsonObject.put("professorId", course.getProfessorId());
        jsonObject.put("credits", course.getCredits());
        jsonObject.put("timings", course.getTimings());
        jsonObject.put("location", course.getLocation());
        jsonObject.put("enrollmentLimit", course.getEnrollmentLimit());
        jsonObject.put("prerequisites", new JSONArray(course.getPrerequisites()));
        jsonObject.put("enrolledStudents", new JSONArray(course.getEnrolledStudents()));
        jsonObject.put("department", course.getDepartment());
        jsonObject.put("Semester", course.getSemester());
        // Add department to JSON
        return jsonObject;
    }

    @Override
    public List<Course> findByProfessorId(String professorId) {
        return findAll().stream()
                .filter(course -> professorId.equals(course.getProfessorId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findAvailableCourses() {
        return findAll().stream()
                .filter(course -> !course.isEnrollmentFull())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStudentEnrolled(String courseCode, String studentId) {
        Optional<Course> course = findById(courseCode);
        return course.map(c -> c.getEnrolledStudents().contains(studentId)).orElse(false);
    }

    @Override
    public void enrollStudent(String courseCode, String studentId) {
        findById(courseCode).ifPresent(course -> {
            if (course.enrollStudent(studentId)) {
                save(course);
            }
        });
    }

    @Override
    public void unenrollStudent(String courseCode, String studentId) {
        findById(courseCode).ifPresent(course -> {
            course.unenrollStudent(studentId);
            save(course);
        });
    }

    @Override
    public List<Course> findCoursesByStudentId(String studentId) {
        return findAll().stream()
                .filter(course -> course.getEnrolledStudents().contains(studentId))
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Course> findByCourseCode(String courseCode) {
        return findAll().stream()
                .filter(course -> course.getCode().equals(courseCode))
                .findFirst();
    }
    
    public boolean isCourseCode(String courseCode) {
    	Optional<Course> course = findByCourseCode(courseCode);
    	return course.isPresent();
    }
    
    @Override
    public void updateCourse(Course course) {
        // Find the existing course by its code
        Optional<Course> existingCourseOpt = findById(course.getCode());
        existingCourseOpt.ifPresent(existingCourse -> {
            // Update the existing course's details
            existingCourse.setTitle(course.getTitle());
            existingCourse.setProfessorId(course.getProfessorId());
            existingCourse.setCredits(course.getCredits());
            existingCourse.setTimings(course.getTimings());
            existingCourse.setLocation(course.getLocation());
            existingCourse.setEnrollmentLimit(course.getEnrollmentLimit());
            existingCourse.getPrerequisites().clear();
            existingCourse.getPrerequisites().addAll(course.getPrerequisites());
            existingCourse.getEnrolledStudents().clear();
            existingCourse.getEnrolledStudents().addAll(course.getEnrolledStudents());
            save(existingCourse); // Save the updated course
        });
    }
    
    @Override
    public List<Course> findCoursesWithAvailableSeats() {
        return findAll().stream()
                .filter(course -> course.getAvailableSeats() > 0)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findCoursesByDepartment(String department) {
        return findAll().stream()
                .filter(course -> department.equals(course.getDepartment()))
                .collect(Collectors.toList());
    }
    
    public synchronized void delete(String id) {
        JSONArray jsonArray = readJsonArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("code").equals(id)) {
                jsonArray.remove(i);
                break;
            }
        }
        writeJsonArray(jsonArray);
    }
}