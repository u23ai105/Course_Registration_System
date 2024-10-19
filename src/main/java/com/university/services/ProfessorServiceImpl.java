package com.university.services;

import com.university.models.Professor;
import com.university.models.Course;
import com.university.models.Student;
import com.university.repositories.ProfessorRepository;
import com.university.repositories.CourseRepository;
import com.university.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository, CourseRepository courseRepository) {
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
		this.studentRepository = new StudentRepository();
    }

    @Override
    public Optional<Professor> findProfessorById(String professorId) {
        return professorRepository.findById(professorId);
    }

    @Override
    public List<Professor> findAllProfessors() {
        return professorRepository.findAll();
    }

    @Override
    public void addProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    @Override
    public void updateProfessor(Professor professor) {
        professorRepository.save(professor); // Assuming save method handles both add and update
    }

    @Override
    public void deleteProfessor(String professorId) {
        professorRepository.delete(professorId);
    }

    @Override
    public List<Professor> findProfessorsByDepartment(String department) {
        return professorRepository.findByDepartment(department);
    }

    @Override
    public void assignCourse(String professorId, String courseCode) {
        Optional<Professor> professorOpt = professorRepository.findById(professorId);
        professorOpt.ifPresent(professor -> {
            professor.assignCourse(courseCode);
            professorRepository.save(professor); // Save the updated professor
        });
    }

    @Override
    public void removeCourseAssignment(String professorId, String courseCode) {
        Optional<Professor> professorOpt = professorRepository.findById(professorId);
        professorOpt.ifPresent(professor -> {
            professor.removeCourse(courseCode);
            professorRepository.save(professor); // Save the updated professor
        });
    }

    @Override
    public List<String> getCoursesTaughtByProfessor(String professorId) {
        return professorRepository.findById(professorId)
                .map(Professor::getTaughtCourses)
                .orElse(List.of()); // Return an empty list if the professor is not found
    }
    
    @Override
    public List<Student> viewCourseRoster(String courseCode) {
        // Retrieve the course by its code
        Optional<Course> courseOpt = courseRepository.findByCourseCode(courseCode);

        // Return the list of students enrolled in the course
        return courseOpt.map(course -> {
            List<String> studentIds = course.getEnrolledStudents(); // Get the list of student IDs
            return studentIds.stream()
                    .map(studentId -> studentRepository.findById(studentId)) // Retrieve Student by ID, returns Optional<Student>
                    .filter(Optional::isPresent) // Keep only present Optionals
                    .map(Optional::get) // Get the Student objects
                    .collect(Collectors.toList()); // Collect to a List<Student>
        }).orElseGet(() -> List.of()); // Return an empty list if the course is not found
    }

	@Override
	public List<Course> viewAssignedCourses(Professor professor) {
	    return professorRepository.findById(professor.getId())
	            .map(prof -> {
	                // Get the list of course codes taught by the professor
	                List<String> courseCodes = prof.getTaughtCourses();
	                // Fetch the corresponding Course objects from the CourseRepository
	                return courseCodes.stream()
	                        .map(courseRepository::findByCourseCode) // This returns Optional<Course>
	                        .filter(Optional::isPresent) // Filter out any empty Optionals
	                        .map(Optional::get) // Get the Course objects
	                        .collect(Collectors.toList()); // Collect into a List<Course>
	            })
	            .orElse(List.of()); // Return an empty list if the professor is not found
	}

	@Override
	public void assignGrade(String studentId, String courseCode, String grade) {
		// TODO Auto-generated method stub
		
	}
}