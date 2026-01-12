package org.example;

import java.util.Optional;

public interface FilmRepository<T extends Film> extends Repository<T> {
    Optional<Film> findByTitle(String title);
}
