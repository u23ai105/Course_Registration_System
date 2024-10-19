package com.university.services;

import com.university.models.Professor;
import com.university.models.Student;
import com.university.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    
    /**
     * Authenticates a user and returns the User object if successful.
     *
     * @param email The user's email address
     * @param password The user's password
     * @return The authenticated User object, or null if authentication fails
     */
	 Optional<User> login(String email, String password);

    /**
     * Logs out the given user.
     *
     * @param user The user to log out
     */
    void logout(User user);

    
    public void registerStudent(Student student);
    
    public void registerProfessor(Professor professor);
    /**
     * Registers a new user in the system.
     *
     * @param user The User object to register
     * @return The registered User object
     * @throws IllegalArgumentException if a user with the same email already exists
     */
    User register(User user) throws IllegalArgumentException;

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find
     * @return An Optional containing the User if found, or an empty Optional if not found
     */
    Optional<User> findById(String id);

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find
     * @return An Optional containing the User if found, or an empty Optional if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves all users in the system.
     *
     * @return A List of all User objects
     */
    List<User> getAllUsers();

    /**
     * Updates the information of an existing user.
     *
     * @param user The User object with updated information
     * @return The updated User object
     * @throws IllegalArgumentException if the user doesn't exist
     */
    User updateUser(User user) throws IllegalArgumentException;

    /**
     * Deletes a user from the system.
     *
     * @param id The ID of the user to delete
     * @throws IllegalArgumentException if the user doesn't exist
     */
    void deleteUser(String id) throws IllegalArgumentException;

    /**
     * Changes the password for a user.
     *
     * @param userId The ID of the user
     * @param oldPassword The current password
     * @param newPassword The new password
     * @return true if the password was successfully changed, false otherwise
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}