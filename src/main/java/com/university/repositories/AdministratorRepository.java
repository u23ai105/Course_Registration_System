package com.university.repositories;

import com.university.models.Administrator;
import java.util.List;
import java.util.Optional;

public interface AdministratorRepository extends Repository<Administrator> {
    Optional<Administrator> findByEmail(String email);
    List<Administrator> findByRole(String role);
}