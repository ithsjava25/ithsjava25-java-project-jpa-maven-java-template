package org.example;

import java.util.Optional;

public interface DirectorRepository<B> extends Repository<Director> {
    Optional<Director> findByName(String name);
}
