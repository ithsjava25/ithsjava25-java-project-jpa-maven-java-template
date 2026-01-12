package org.example;

import jakarta.persistence.EntityManager;

import java.util.Optional;

public class FilmRepositoryImpl extends BaseRepositoryImpl<Film> implements FilmRepository<Film> {

    public FilmRepositoryImpl(EntityManager em) {
        super(em.createEntityManager(), Film.class);
    }

    @Override
    public Optional<Film> findByTitle(String title) {
        return em.createQuery("SELECT f FROM Film f WHERE f.title = :title", Film.class)
            .setParameter("title", title)
            .getResultList().stream()
            .findFirst();

    }
}
