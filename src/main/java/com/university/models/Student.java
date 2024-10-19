package com.university.models;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int currentSemester;
    private List<String> enrolledCourses;
    private List<String> completedCourses;
    private Map<String, String> grades;
    private int totalCredits;
    

    public Student(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.currentSemester = 1;
        this.enrolledCourses = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.grades = new HashMap<>();
        this.totalCredits = 0;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    // Getters
    public int getCurrentSemester() {
        return currentSemester;
    }
    
    public int gettotalCredits() {
        return totalCredits; // Getter for total credits
    }

    public void resettotalCredits() {
        totalCredits = 0; // Reset total credits when advancing to the next semester
    }

    public List<String> getEnrolledCourses() {
    	System.out.println(enrolledCourses);
        return new ArrayList<>(enrolledCourses);
    }

    public List<String> getCompletedCourses() {
        return new ArrayList<>(completedCourses);
    }

    // Setters
    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public void settotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }
    
    // Methods to manage courses
    
    public boolean enrollCourse(String courseCode, int credits) {
        if (!enrolledCourses.contains(courseCode) && (totalCredits + credits <= 20)) {
            enrolledCourses.add(courseCode);
            totalCredits += credits; // Add credits to total
            return true; // Successfully enrolled
        }
        return false; // Enrollment failed
    }
    
//    public void enrollCourse(String courseCode,int credits) {
//        if (!enrolledCourses.contains(courseCode)) {
//            enrolledCourses.add(courseCode);
//    	}
//    }
    
    public void enrollCourse(String courseCode) {
        if (!enrolledCourses.contains(courseCode)) {
            enrolledCourses.add(courseCode);
        }

    }

//    public void dropCourse(String courseCode) {
//        enrolledCourses.remove(courseCode);
//    }
    
    public void dropCourse(String courseCode, int credits) {
        if (enrolledCourses.remove(courseCode)) {
            totalCredits -= credits; // Subtract credits when dropping a course
        }
    }

    public void completeCourse(String courseCode) {
        if (enrolledCourses.remove(courseCode)) {
            completedCourses.add(courseCode);
        }
    }

    public boolean isEnrolledIn(String courseCode) {
        return enrolledCourses.contains(courseCode);
    }

    public boolean hasCompleted(String courseCode) {
        return completedCourses.contains(courseCode);
    }

    // Method to advance to next semester
    public void advanceToNextSemester() {
        currentSemester++;
        // Move all enrolled courses to completed courses
        completedCourses.addAll(enrolledCourses);
        enrolledCourses.clear();
    }

    public void setGrade(String courseCode, String grade) {
        grades.put(courseCode, grade);
        
        // If a grade is set, we assume the course is completed
        if (!completedCourses.contains(courseCode)) {
            completedCourses.add(courseCode);
        }
        
        // Remove from enrolled courses if it's there
        enrolledCourses.remove(courseCode);
    }
    
    public String getGrade(String courseCode) {
        return grades.get(courseCode);
    }
    
    public Map<String, String> getGrades() {
        return new HashMap<>(grades);  // Return a copy to preserve encapsulation
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", currentSemester=" + currentSemester +
                ", enrolledCourses=" + enrolledCourses +
                ", completedCourses=" + completedCourses +
                '}';
    }

    // You might want to override equals() and hashCode() if needed
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return getCurrentSemester() == student.getCurrentSemester();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getCurrentSemester();
        return result;
    }
}