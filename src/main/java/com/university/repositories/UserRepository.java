package com.university.repositories;

import com.university.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User> {
    
    // Inherited methods from Repository<User>:
    // List<User> findAll();
    // Optional<User> findById(String id);
    // void save(User entity);
    // void delete(String id);

    // User-specific methods
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(String role);
    
    boolean existsByEmail(String email);
    
    void updatePassword(String userId, String newPassword);
    
    List<User> findUsersByName(String name);
}