package com.university.models;
import com.university.repositories.ProfessorRepository;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String code;
    private String title;
    private String professorId;
    private int credits;
    private List<String> prerequisites;
    private String timings;
    private String location;
    private int enrollmentLimit;
    private List<String> enrolledStudents;
    private String department;
    private int Semester;

    public Course(String code, String title, int credits, String timings, String location, int enrollmentLimit, String department,int Semester) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.timings = timings;
        this.location = location;
        this.enrollmentLimit = enrollmentLimit;
        this.prerequisites = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.department = department;
        this.Semester=Semester;// Initialize the department
    }

    // Getters
    
    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getProfessorId() {
        return professorId;
    }

    public int getCredits() {
        return credits;
    }

    public List<String> getPrerequisites() {
        return new ArrayList<>(prerequisites);
    }

    public String getTimings() {
        return timings;
    }

    public String getLocation() {
        return location;
    }

    public int getEnrollmentLimit() {
        return enrollmentLimit;
    }

    public List<String> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents);
    }
    
    public String getDepartment() {
        return department; // Getter for department
    }
    
    public int getSemester() {
        return Semester;
    }
    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public void setProfessor(Professor professor) {
        this.professorId = professor.getId();
    }
    
    public Professor getProfessor(ProfessorRepository professorRepository) {
        return professorId != null ? professorRepository.findById(professorId).orElse(null) : null;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }
    
    public void setSemester(int semester) {
        this.Semester = semester; // Setter method for semester
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEnrollmentLimit(int enrollmentLimit) {
        this.enrollmentLimit = enrollmentLimit;
    }
    
    public void setDepartment(String department) {
        this.department = department; // Setter for department
    }

    // Methods to manage prerequisites
    public void addPrerequisite(String courseCode) {
        if (!prerequisites.contains(courseCode)) {
            prerequisites.add(courseCode);
        }
    }

    public void removePrerequisite(String courseCode) {
        prerequisites.remove(courseCode);
    }

    // Methods to manage enrolled students
    public boolean enrollStudent(String studentId) {
        if (enrolledStudents.size() < enrollmentLimit && !enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            return true;
        }
        return false;
    }

    public void unenrollStudent(String studentId) {
        enrolledStudents.remove(studentId);
    }

    public boolean isEnrollmentFull() {
        return enrolledStudents.size() >= enrollmentLimit;
    }

    public int getAvailableSeats() {
        return enrollmentLimit - enrolledStudents.size();
    }
    
    public boolean isprerequisteexist(String pre) {
    	for(String preq: prerequisites) {
    		if(preq.equals(pre)) return true;
    	}
    	return false;
    }

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", professorId='" + professorId + '\'' +
                ", credits=" + credits +
                ", prerequisites=" + prerequisites +
                ", timings='" + timings + '\'' +
                ", location='" + location + '\'' +
                ", enrollmentLimit=" + enrollmentLimit +
                ", enrolledStudents=" + enrolledStudents +
                ", department='" + department + '\'' + 
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getCode().equals(course.getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }
}