package com.university.models;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private List<String> taughtCourses;
    private String department; 

    public Professor(String id, String name, String email, String password,String department) {
        super(id, name, email, password);
        this.department=department;
        this.taughtCourses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Professor";
    }

    public String getId() {
        return id;
    }
    
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department; // Setter for department
    }
    
    // Getter for taughtCourses
    public List<String> getTaughtCourses() {
        return new ArrayList<>(taughtCourses);
    }

    // Methods to manage taught courses
    public void addCourse(String courseCode) {
        if (!taughtCourses.contains(courseCode)) {
            taughtCourses.add(courseCode);
        }
    }

    public void removeCourse(String courseCode) {
        taughtCourses.remove(courseCode);
    }

    public boolean isTeaching(String courseCode) {
        return taughtCourses.contains(courseCode);
    }
    
    public void assignCourse(String courseCode) {
        addCourse(courseCode); // Use the existing addCourse method
    }
    
    public void removeCourseAssignment(String courseCode) {
        removeCourse(courseCode); // Use the existing removeCourse method
    }

    // Method to get the number of courses taught
    public int getNumberOfCoursesTaught() {
        return taughtCourses.size();
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", taughtCourses=" + taughtCourses +
                '}';
    }

    // You might want to override equals() and hashCode() if needed
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        if (!super.equals(o)) return false;
        Professor professor = (Professor) o;
        return getTaughtCourses().equals(professor.getTaughtCourses());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getTaughtCourses().hashCode();
        return result;
    }
}