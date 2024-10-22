package com.university.ui;

import com.university.models.User;
import com.university.repositories.ComplaintRepository;
import com.university.repositories.ComplaintRepository;
import com.university.models.Student;
import com.university.models.Professor;
import com.university.models.Administrator;
import com.university.models.Complaint;
import com.university.models.Course; 
import com.university.services.UserService;
import com.university.services.StudentService;
import com.university.services.ProfessorService;
import com.university.services.AdministratorService;

import java.util.Optional;
import java.util.List; 
import java.util.Scanner;

public class RegistrationSystem {
    private UserService userService;
    private StudentService studentService;
    private ProfessorService professorService;
    private AdministratorService administratorService;
    private Scanner scanner;

    public RegistrationSystem(UserService userService, StudentService studentService, 
                              ProfessorService professorService, AdministratorService administratorService) {
        this.userService = userService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.administratorService = administratorService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Registration System!");
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            // Login
            login(scanner);
        } else if (choice == 2) {
            // Create New Account
            createAccount(scanner);
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Email: "); // Change from Username to Email
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> userOptional = userService.login(email, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get(); // Extract the User object
            System.out.println("Login successful! Welcome, " + user.getName());
            showUserMenu(user); // Pass the User object to showUserMenu
        } else {
            System.out.println("Invalid email or password.");
        }
    }
    
    private void createAccount(Scanner scanner) {
        System.out.println("Choose account type:");
        System.out.println("1. Student");
        System.out.println("2. Professor");
        System.out.print("Select account type: ");
        int accountType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter department: ");
        String department=scanner.nextLine();

        if (accountType == 1) {
            // Create Student Account
            createStudentAccount(username, password, email);
        } else if (accountType == 2) {
            // Create Professor Account
            createProfessorAccount(username, password, email,department);
        }
        else {
            System.out.println("Invalid account type selected.");
        }
    }
    
    private void createStudentAccount(String username, String password, String email) {
        if (userService.existsByEmail(email)) {
            System.out.println("Email already in use. Please try a different email.");
            return;
        }

        // Create a new Student object
        Student newStudent = new Student(generateId(), username, email, password);
        userService.registerStudent(newStudent); // Assuming you have a method to register a student
        System.out.println("Student account created successfully!");
    }

    private void createProfessorAccount(String username, String password, String email,String department) {
        if (userService.existsByEmail(email)) {
            System.out.println("Email already in use. Please try a different email.");
            return;
        }
        Professor newProfessor = new Professor(generateId(), username, email, password,department);
        userService.registerProfessor(newProfessor); // Assuming you have a method to register a professor
        System.out.println("Professor account created successfully!");
    }
    
    private String generateId() {
        // Implement a method to generate a unique ID
        return "USER" + System.currentTimeMillis(); // Simple ID generation for demonstration
    }

    private void showUserMenu(User user) {
        switch (user.getRole()) {
            case "Student":
                showStudentMenu((Student) user);
                break;
            case "Professor":
                showProfessorMenu((Professor) user);
                break;
            case "Administrator":
                showAdministratorMenu((Administrator) user);
                break;
            default:
                System.out.println("Unknown user role.");
        }
    }

    private void showStudentMenu(Student student) {
        while (true) {
            System.out.println("\nStudent Menu");
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. View Enrolled Courses");
            System.out.println("4. Drop a Course");
            System.out.println("5. View Grades");
            System.out.println("6. Submit a Complaint");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAvailableCourses();
                    break;
                case 2:
                    enrollInCourse(student);
                    break;
                case 3:
                    viewEnrolledCourses(student);
                    break;
                case 4:
                    dropCourse(student);
                    break;
                case 5:
                    viewGrades(student);
                    break;
                case 6:
                    viewcomplaints(student);
                    break;    
                case 7:
                    submitComplaint(student);
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //1.view available course
    private void viewAvailableCourses() {
        List<Course> availableCourses = studentService.viewAvailableCourses();
        if (availableCourses.isEmpty()) {
            System.out.println("No available courses at the moment.");
        } else {
            System.out.println("Available Courses:");
            for (Course course : availableCourses) {
                System.out.printf("Code: %s, Title: %s, Credits: %d, Timings: %s%n",
                        course.getCode(), course.getTitle(), course.getCredits(), course.getTimings());
            }
        }
    }
    
    // 2. enroll courses 
    private void enrollInCourse(Student student) {
        System.out.print("Select your semester (1, 2, ...): ");
        int semester=student.getCurrentSemester();
//        int semester = scanner.nextInt();
//        scanner.nextLine(); // Consume newline

        // Fetch available courses for the selected semester
        List<Course> availableCourses = studentService.viewAvailableCoursesForSemester(semester);
        if (availableCourses.isEmpty()) {
            System.out.println("No available courses for semester " + semester);
            return;
        }

        // Display available courses
        System.out.println("Available Courses for Semester " + semester + ":");
        for (Course course : availableCourses) {
            System.out.printf("Code: %s, Title: %s, Credits: %d%n", course.getCode(), course.getTitle(), course.getCredits());
        }

        System.out.print("Enter the course code to enroll: ");
        String courseCode = scanner.nextLine();
        int credits=0;
        for (Course course : availableCourses) {
        	System.out.println(course.getCredits());
        	if(course.getCode().equals(courseCode)) credits=course.getCredits();
        	System.out.println(credits);
           // System.out.printf("Code: %s, Title: %s, Credits: %d%n", course.getCode(), course.getTitle(), course.getCredits());
        }
        boolean success = studentService.enrollInCourse(student, courseCode,credits);
        if (success) {
            System.out.println("Successfully enrolled in " + courseCode);
        } else {
            System.out.println("Failed to enroll in " + courseCode + ". Please check prerequisites or enrollment limits.");
        }
    }
    
    //3. view enrolled courses
    private void viewEnrolledCourses(Student student) {
        List<Course> enrolledCourses = studentService.viewEnrolledCourses(student);
        if (enrolledCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Enrolled Courses:");
            for (Course course : enrolledCourses) {
                System.out.printf("Code: %s, Title: %s, Credits: %d Timings: %s%n",
                        course.getCode(), course.getTitle(), course.getCredits(), course.getTimings());
            }
        }
    }
    
    //4. drop courses
    private void dropCourse(Student student) {
   	 List<String> enrolledCourses=student.getEnrolledCourses();
   	 for(String Course : enrolledCourses) System.out.println(Course);
   	 System.out.println("Enter the course code to drop it");
   	 String courseCode=scanner.nextLine();
   	 int credits=0;
   	 List<Course> availableCourses = studentService.viewAvailableCoursesForSemester(student.getCurrentSemester());
        for (Course course : availableCourses) {
        	//System.out.println(course.getCredits());
        	if(course.getCode().equals(courseCode)) credits=course.getCredits();
        	//System.out.println(credits);
           // System.out.printf("Code: %s, Title: %s, Credits: %d%n", course.getCode(), course.getTitle(), course.getCredits());
        }
        boolean success = studentService.dropCourse(student, courseCode,credits);
        if (success) {
            System.out.println("Successfully dropped the " + courseCode);
        } else {
            System.out.println("Failed to drop the " + courseCode);
        }
   }
    
    //5. view enrolled grades
    private void viewGrades(Student student) {
        List<String> grades = studentService.viewGrades(student);
        if (grades.isEmpty()) {
            System.out.println("No grades available.");
        } else {
            System.out.println("Your Grades:");
            for (String grade : grades) {
                System.out.println(grade);
            }
        }
    }
    
    //6. view complaints
    private void viewcomplaints(Student student) {
    	List<Complaint> complaints=studentService.viewComplaint(student);
    	if (complaints.isEmpty()) {
            System.out.println("No grades available.");
        } else {
            System.out.println("Your Grades:");
            for (Complaint complaint : complaints) {
            	System.out.println(complaint.getId());
                System.out.println(complaint.getStudentId());
                System.out.println(complaint.getDescription());
                System.out.println(complaint.getStatus());
                System.out.println(complaint.getSubmissionDate());
            }
        }
    }
    
    //7. submit complaints
    private void submitComplaint(Student student) {
        System.out.print("Enter your complaint: ");
        String complaintText = scanner.nextLine();
        // Assuming there's a method in the StudentService to submit a complaint
        studentService.submitComplaint(student, complaintText);
        System.out.println("Complaint submitted successfully!");
    }

    private void showProfessorMenu(Professor professor) {
        while (true) {
            System.out.println("\nProfessor Menu");
            System.out.println("1. View Assigned Courses");
            System.out.println("2. View Course Roster");
            System.out.println("3. Assign Grades");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                	viewassignedcourses(professor);
                    break;
                case 2:
                    viewCourseRoster(professor);
                    break;
                case 3:
                    assignGrades(professor);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showAdministratorMenu(Administrator administrator) {
        while (true) {
            System.out.println("\nAdministrator Menu");
            System.out.println("1. Manage Course Catalog");
            System.out.println("2. Manage Student Records");
            System.out.println("3. Assign Professors to Courses");
            System.out.println("4. Handle Complaints");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageCourses();
                    break;
                case 2:
                    manageStudentRecords();
                    break;
                case 3:
                    //assignProfessorsToCourses();
                    break;
                case 4:
                    //handleComplaints();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private void viewassignedcourses(Professor professor) {
    	List<Course> availableCourses=professorService.viewAssignedCourses(professor);
    	if(availableCourses.isEmpty()) {
    		System.out.println("NO Courses are assigned");
    		return;
    	}
   
    	System.out.println(availableCourses);
    	for (Course course : availableCourses) {
         	//System.out.println(course.getCredits());
         	//if(course.getCode().equals(courseCode)) credits=course.getCredits();
         	//System.out.println(credits);
    		System.out.println(2);
            System.out.printf("Code: %s, Title: %s, Credits: %d%n", course.getCode(), course.getTitle(), course.getCredits());
         }
    }
    

    // Helper methods for professor actions
    private void viewCourseRoster(Professor professor) {
        System.out.print("Enter the course code to view the roster: ");
        String courseCode = scanner.nextLine();
        
        List<Course> availableCourses=professorService.viewAssignedCourses(professor);
        boolean check=false;
        for (Course course : availableCourses) {
        if(course.getCode().equals(courseCode)) check=true;
        }
        if(check==false){
        	System.out.println("The course is not assigned to this profesor");
        	return;
        }
        List<Student> enrolledStudents = professorService.viewCourseRoster(courseCode);
        if (enrolledStudents.isEmpty()) {
            System.out.println("No students are enrolled in this course.");
        } else {
            System.out.println("Enrolled Students:");
            for (Student student : enrolledStudents) {
                System.out.printf("ID: %s, Name: %s, Email: %s%n", student.getId(), student.getName(), student.getEmail());
            }
        }
    }

    private void assignGrades(Professor professor) {
        System.out.print("Enter the course code for which you want to assign grades: ");
        String courseCode = scanner.nextLine();
        
        List<Course> availableCourses=professorService.viewAssignedCourses(professor);
        boolean check=false;
        for (Course course : availableCourses) {
        if(course.getCode().equals(courseCode)) check=true;
        }
        if(check==false){
        	System.out.println("The course is not assigned to this profesor");
        	return;
        }
        
        List<Student> enrolledStudents = professorService.viewCourseRoster(courseCode);
        if (enrolledStudents.isEmpty()) {
            System.out.println("No students are enrolled in this course.");
            return;
        }

        for (Student student : enrolledStudents) {
            System.out.printf("Enter grade for %s (ID: %s): ", student.getName(), student.getId());
            String grade = scanner.nextLine();
            professorService.assignGrade(student.getId(), courseCode, grade);
            System.out.println("Grade assigned successfully for " + student.getName());
        }
    }

    // Helper methods for administrator actions
    private void manageCourses() {
        System.out.println("Course Management Menu:");
        System.out.println("1. Add Course");
        System.out.println("2. Update Course");
        System.out.println("3. Delete Course");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addCourse();
                break;
            case 2:
                updateCourse();
                break;
            case 3:
                deleteCourse();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void addCourse() {
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        if(administratorService.iscourseexist(code)) {
        	System.out.println("This course already exist");
        	return;
        }
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        System.out.print("Enter credits: ");
        int credits = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter timings: ");
        String timings = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter enrollment limit: ");
        int enrollmentLimit = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter department: ");
        String department = scanner.nextLine();
        System.out.print("Enter semester: ");
        int semester = scanner.nextInt();
        scanner.nextLine(); 
        // Consume newline
        
        Course newCourse = new Course(code, title, credits, timings, location, enrollmentLimit, department, semester);
        if (semester >= 2) {
            boolean check = true;
            while (check) {
                System.out.println("Enter 1 if you want to add more prerequisites");
                System.out.println("Enter any other value if you do not want to add more prerequisites");
                int temp = scanner.nextInt();
                scanner.nextLine(); 

                if (temp == 1) {
                    System.out.print("Enter prerequisite course code: ");
                    String preq = scanner.nextLine(); 
                    newCourse.addPrerequisite(preq); 
                } else {
                    check = false; 
                }
            }
        }
        administratorService.addCourse(newCourse);
        System.out.println("Course added successfully!");
    }

    private void updateCourse() {
        System.out.print("Enter course code to update: ");
        String code = scanner.nextLine();
        // Fetch the existing course
        Optional<Course> existingCourseOpt = administratorService.findByCourseCode(code);
        if (existingCourseOpt.isPresent()) {
            Course existingCourse = existingCourseOpt.get();
            System.out.print("Enter new title (leave blank to keep current): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) existingCourse.setTitle(title);
            System.out.print("Enter new credits (leave blank to keep current): ");
            String creditsInput = scanner.nextLine();
            if (!creditsInput.isEmpty()) existingCourse.setCredits(Integer.parseInt(creditsInput));
            System.out.print("Enter new timings (leave blank to keep current): ");
            String timings = scanner.nextLine();
            if (!timings.isEmpty()) existingCourse.setTimings(timings);
            System.out.print("Enter new location (leave blank to keep current): ");
            String location = scanner.nextLine();
            if (!location.isEmpty()) existingCourse.setLocation(location);
            System.out.print("Enter new enrollment limit (leave blank to keep current): ");
            String enrollmentLimitInput = scanner.nextLine();
            if (!enrollmentLimitInput.isEmpty()) existingCourse.setEnrollmentLimit(Integer.parseInt(enrollmentLimitInput));
            System.out.print("Enter new department (leave blank to keep current): ");
            String department = scanner.nextLine();
            if (!department.isEmpty()) existingCourse.setDepartment(department);
            System.out.print("Enter new semester (leave blank to keep current): ");
            String semesterInput = scanner.nextLine();
            if (!semesterInput.isEmpty()) existingCourse.setSemester(Integer.parseInt(semesterInput));
            while(true) {
            System.out.println("enter 1 to add prerequistes");
            System.out.println("else 0");
            int val=scanner.nextInt();
            scanner.nextLine();
            if(val==0) break;
            System.out.println("Enter the prerequiste to be updated or added");
            String preq;
            preq=scanner.nextLine();
            if(existingCourse.isprerequisteexist(preq)) {
            	System.out.println("This prerequiste already exists");
            }
            else{
            	existingCourse.addPrerequisite(preq);
            	System.out.println("The prerequiste is added sucessfully");
            }
            
            }
            administratorService.updateCourse(existingCourse);
            //System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }

    private void deleteCourse() {
        System.out.print("Enter course code to delete: ");
        String code = scanner.nextLine();
        administratorService.deleteCourse(code);
       // System.out.println("Course deleted successfully!");
    }

    private void manageStudentRecords() {
        System.out.println("Student Records Management Menu:");
        System.out.println("1. View All Students");
        System.out.println("2. Update Student Info");
        System.out.println("3. Delete Student");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewAllStudents();
                break;
            case 2:
                updateStudentInfo();
                break;
            case 3:
                deleteStudent();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void viewAllStudents() {
        List<Student> students = administratorService.viewAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("List of Students:");
            for (Student student : students) {
                System.out.printf("ID: %s, Name: %s, Email: %s%n", student.getId(), student.getName(), student.getEmail());
            }
        }
    }

    private void updateStudentInfo() {
        System.out.print("Enter student ID to update: ");
        String studentId = scanner.nextLine();
        
        // Retrieve the student using the service
        Optional<Student> studentOpt = studentService.getByStudentId(studentId);
        
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get(); // Get the Student object
            System.out.println("Current Student Information:");
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Current Semester: " + student.getCurrentSemester());
            System.out.println("Total Credits: " + student.gettotalCredits());
            System.out.println("Enrolled Courses: " + String.join(", ", student.getEnrolledCourses()));
            
            // Update student details
            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) student.setName(name);
            
            System.out.print("Enter new email (leave blank to keep current): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) student.setEmail(email);
            
            System.out.print("Enter new current semester (leave blank to keep current): ");
            String semesterInput = scanner.nextLine();
            if (!semesterInput.isEmpty()) {
                int newSemester = Integer.parseInt(semesterInput);
                student.setCurrentSemester(newSemester);
            }
            
            System.out.print("Enter new total credits (leave blank to keep current): ");
            String creditsInput = scanner.nextLine();
            if (!creditsInput.isEmpty()) {
                int newTotalCredits = Integer.parseInt(creditsInput);
                student.settotalCredits(newTotalCredits);
            }
            
            // Optionally update enrolled courses
            System.out.print("Do you want to update enrolled courses? (yes/no): ");
            String updateCoursesResponse = scanner.nextLine();
            if (updateCoursesResponse.equalsIgnoreCase("yes")) {
                System.out.print("Enter enrolled courses (comma-separated): ");
                String coursesInput = scanner.nextLine();
                String[] courses = coursesInput.split(",");
                for (String course : courses) {
                    student.enrollCourse(course.trim()); // Trim whitespace and enroll
                }
            }

            // Call the service to update the student information
            administratorService.updateStudent(student);
            System.out.println("Student information updated successfully!");
        } else {
            System.out.println("Student not found.");
        }
    }

    private void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        String studentId = scanner.nextLine();
        administratorService.deletestudent(studentId);
        System.out.println("Student deleted successfully!");
    }

//    private void assignProfessorsToCourses() {
//        System.out.print("Enter course code to assign a professor: ");
//        String courseCode = scanner.nextLine();
//        System.out.print("Enter professor ID to assign: ");
//        String professorId = scanner.nextLine();
//
//        Optional<Course> courseOpt = administratorService.findCourseByCode(courseCode);
//        Optional<Professor> professorOpt = professorService.findProfessorById(professorId);
//
//        if (courseOpt.isPresent() && professorOpt.isPresent()) {
//            Course course = courseOpt.get();
//            Professor professor = professorOpt.get();
//            administratorService.assignProfessorToCourse(professor, course);
//            System.out.println("Professor assigned to course successfully!");
//        } else {
//            System.out.println("Course or Professor not found.");
//        }
//    }

//    private void handleComplaints() {
//        List<Complaint> complaints = administratorService.getAllComplaints();
//        if (complaints.isEmpty()) {
//            System.out.println("No complaints to handle.");
//            return;
//        }
//
//        System.out.println("Complaints:");
//        for (Complaint complaint : complaints) {
//            System.out.printf("ID: %s, Student ID: %s, Text: %s, Status: %s%n",
//                    complaint.getId(), complaint.getStudentId(), complaint.getText(), complaint.getStatus());
//        }
//
//        System.out.print("Enter complaint ID to update status: ");
//        String complaintId = scanner.nextLine();
//        System.out.print("Enter new status: ");
//        String status = scanner.nextLine();
//        administratorService.updateComplaintStatus(complaintId, status);
//        System.out.println("Complaint status updated successfully!");
//    }
}