package com.university.ui;
import java.util.Scanner;
import com.university.repositories.*;
import com.university.services.*;
import com.university.ui.RegistrationSystem;

public class Main {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
        // Initialize repositories
        StudentRepository studentRepository = new StudentRepository();
        CourseRepository courseRepository = new CourseRepositoryImpl();
        ProfessorRepository professorRepository = new ProfessorRepositoryImpl();
        AdministratorRepository administratorRepository = new AdministratorRepositoryImpl();
        ComplaintRepository complaintRepository = new ComplaintRepositoryImpl();

        // Initialize services
        UserService userService = new UserServiceImpl(studentRepository, professorRepository, administratorRepository);
        StudentService studentService = new StudentServiceImpl(studentRepository, courseRepository, complaintRepository);
        ProfessorService professorService = new ProfessorServiceImpl(professorRepository, courseRepository);
        AdministratorService administratorService = new AdministratorServiceImpl(administratorRepository, studentRepository, courseRepository, professorRepository, complaintRepository);

        // Initialize and start the registration system
        RegistrationSystem registrationSystem = new RegistrationSystem(userService, studentService, professorService, administratorService);
        System.out.println("Welcome to the University application");
        System.out.println("Enter 1 to enter the registration system");
        System.out.println("Enter 0 to exit");
        int recurr=scanner.nextInt();
        while(recurr==1) {
        registrationSystem.start();
        System.out.println("Enter 1 to enter the registration system");
        System.out.println("Enter 0 to exit");
        recurr=scanner.nextInt();
        if(recurr==0) System.out.println("logged out sucessfully");
        }
    }
}