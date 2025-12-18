package org.example.repo;

import jakarta.persistence.EntityManagerFactory;
import org.example.PersistenceManager;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Song;

import java.util.List;

public class ArtistRepositoryImpl implements ArtistRepository {

    private final EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();

    @Override
    public boolean existsByUniqueId(Artist artist) {
        return emf.callInTransaction(em ->
            em.createQuery("select count(a) from Artist a where a.artistId = :artistId", Long.class)
                .setParameter("artistId", artist.getArtistId())
                .getSingleResult() > 0
        );
    }

    @Override
    public void save(Artist artist) {
        emf.runInTransaction(em -> em.persist(artist));
    }

    @Override
    public List<Artist> findAll() {
        return emf.callInTransaction(em ->
            em.createQuery("select a from Artist a", Artist.class)
                .getResultList());
    }

    @Override
    public Long count() {
        return emf.callInTransaction(em ->
            em.createQuery("select count(a) from Artist a", Long.class)
                .getSingleResult());
    }
}
