package org.example.repo;

import jakarta.persistence.EntityManagerFactory;
import org.example.PersistenceManager;
import org.example.entity.Artist;

import java.util.List;

public class ArtistRepositoryImpl implements ArtistRepository{

    private final EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();

    @Override
    public void save(Artist artist) {
        emf.runInTransaction(em -> em.persist(artist));
    }

    @Override
    public Long count() {
        return 0L;
    }

    @Override
    public List<Artist> findAll() {
        return List.of();
    }


    @Override
    public boolean existsByUniqueId(Artist artist) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("select count(a) from Artist a where a.artistId = :artistId", Long.class)
                .setParameter("artistId", artist.getArtistId())
                .getSingleResult() > 0;
        }
    }
}
