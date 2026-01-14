package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.Optional;

public class DirectorRepositoryImpl extends BaseRepositoryImpl<Director> implements DirectorRepository<Director> {

    public DirectorRepositoryImpl(EntityManagerFactory em) {

        super(em.createEntityManager(), Director.class);
    }

    @Override
    public Optional<Director> findByName(String name) {

        return em.createQuery("SELECT d FROM Director d WHERE d.name = :name", Director.class)
            .setParameter("name", name)
            .getResultStream()
            .findFirst();
    }


}
