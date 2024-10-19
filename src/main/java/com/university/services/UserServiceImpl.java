package com.university.services;

import com.university.models.Student;
import com.university.models.Professor;
import com.university.models.Administrator;
import com.university.models.User;
import com.university.repositories.Repository;
import com.university.repositories.StudentRepository;
import com.university.repositories.ProfessorRepository;
import com.university.repositories.AdministratorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
	private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final AdministratorRepository administratorRepository;
    private Repository<User> userRepository;

//    public UserServiceImpl(Repository<User> userRepository) {
//        this.userRepository = userRepository;
//    }
    
    public UserServiceImpl(StudentRepository studentRepository, ProfessorRepository professorRepository, AdministratorRepository administratorRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.administratorRepository = administratorRepository;
    }

    
    public Optional<User> login(String email, String password) {
        // Check in Student Repository
        Optional<Student> student = studentRepository.findAll().stream()
                .filter(s -> s.getEmail().equals(email) && s.getPassword().equals(password))
                .findFirst();

        if (student.isPresent()) {
            return Optional.of(student.get()); // Return the Student as Optional<User>
        }

        // Check in Professor Repository
        Optional<Professor> professor = professorRepository.findAll().stream()
                .filter(p -> p.getEmail().equals(email) && p.getPassword().equals(password))
                .findFirst();

        if (professor.isPresent()) {
            return Optional.of(professor.get()); // Return the Professor as Optional<User>
        }
        
        Optional<Administrator> administrator = administratorRepository.findAll().stream()
                .filter(p -> p.getEmail().equals(email) && p.getPassword().equals(password))
                .findFirst();

        if (administrator.isPresent()) {
            return Optional.of(administrator.get()); // Return the Professor as Optional<User>
        }

        // If no user found, return an empty Optional
        return Optional.empty();
    }
//    @Override
//    public User login(String email, String password) {
//        return userRepository.findAll().stream()
//            .filter(user -> user.getEmail().equals(email) && user.checkPassword(password))
//            .findFirst()
//            .orElse(null);
//    }

    @Override
    public void logout(User user) {
        // In a stateless system, logout might not require any action
        // If you have a session management system, you would invalidate the session here
        System.out.println("User " + user.getName() + " logged out");
    }

    @Override
    public User register(User user) throws IllegalArgumentException {
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("A user with this email already exists");
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findAll().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) throws IllegalArgumentException {
        if (!userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(String id) throws IllegalArgumentException {
        if (!userRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.delete(id);
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.checkPassword(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email)) ||
               professorRepository.findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    // Helper method to get users by role
    public List<User> getUsersByRole(String role) {
        return userRepository.findAll().stream()
            .filter(user -> user.getRole().equals(role))
            .collect(Collectors.toList());
    }
    
    public void registerStudent(Student student) {
        studentRepository.save(student); // Save the new student to the repository
    }

    public void registerProfessor(Professor professor) {
        professorRepository.save(professor); // Save the new professor to the repository
    }
}