package org.example;

import java.util.Optional;

public interface Repository<T extends org.example.BaseEntity> {
    T save(T entity);
    Optional<T> findById(Long id);
    void delete(T entity);
    void deleteById(Long id);
    Iterable<T> findAll();
    boolean existsById(Long id);

    long count();

    void flush();

    void clear();
}
