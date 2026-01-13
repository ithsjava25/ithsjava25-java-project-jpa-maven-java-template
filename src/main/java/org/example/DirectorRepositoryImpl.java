package org.example;

import jakarta.persistence.EntityManagerFactory;

import java.util.Optional;

//public class DirectorRepositoryImpl extends BaseRepositoryImpl<Director> implements DirectorRepository<Director> {

public class DirectorRepositoryImpl extends BaseRepositoryImpl<Director> implements DirectorRepository<Director> {
    private final EntityManagerFactory emf;

    public DirectorRepositoryImpl(EntityManagerFactory em) {

        //super(em.createEntityManager(), Director.class);
        super(Director.class);
        this.emf = em;
    }

    @Override
    public Optional<Director> findByName(String name) {
        return em.createQuery("SELECT d FROM Director d WHERE d.name = :name", Director.class)
            .setParameter("name", name)
            .getResultStream()
            .findFirst();

    }
}
