package org.example;

import java.util.Optional;

public interface DirectorRepository<T extends Director> extends Repository<T> {
    Optional<Director> findByName(String name);
}
