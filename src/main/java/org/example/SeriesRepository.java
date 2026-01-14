package org.example;

import java.util.Optional;

public interface SeriesRepository<T extends Series> extends Repository<T> {
    Optional<Series> findByName(String name);
}
