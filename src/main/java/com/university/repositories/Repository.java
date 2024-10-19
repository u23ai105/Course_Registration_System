package com.university.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> findAll();
    Optional<T> findById(String id);
    void save(T entity);
    void delete(String id);
}