package org.example.repo;

import jakarta.persistence.EntityManagerFactory;
import org.example.PersistenceManager;
import org.example.entity.Album;
import org.example.entity.Artist;

import java.util.List;

public class AlbumRepositoryImpl implements AlbumRepository{

    private final EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();

    @Override
    public void save(Album album) {
        emf.runInTransaction(em -> em.persist(album));
    }

    @Override
    public Long count() {
        return 0L;
    }

    @Override
    public List<Album> findAll() {
        return List.of();
    }

    @Override
    public Album findByArtist(Artist artist) {
        return null;
    }

    @Override
    public Album findByGenre(String genre) {
        return null;
    }

    @Override
    public boolean existsByUniqueId(Album album) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("select count(a) from Album a where a.albumId = :albumId", Long.class)
                .setParameter("albumId", album.getAlbumId())
                .getSingleResult() > 0;
        }
    }


}
