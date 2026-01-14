package org.example;

import jakarta.persistence.EntityManagerFactory;

import java.util.Optional;

public class SeriesRepositoryImpl extends BaseRepositoryImpl<Series> implements SeriesRepository<Series> {

    private final EntityManagerFactory emf;

    public SeriesRepositoryImpl(EntityManagerFactory em) {
        super(Series.class);
        this.emf = em;
    }

    @Override
    public Optional<Series> findByTitle(String title){
        return em.createQuery("SELECT s FROM Series s WHERE s.title = :title", Series.class)
            .setParameter("title", title)
            .getResultStream()
            .findFirst();
    }
}
