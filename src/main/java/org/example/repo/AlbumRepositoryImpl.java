package org.example.repo;

import jakarta.persistence.EntityManagerFactory;
import org.example.PersistenceManager;
import org.example.entity.Album;
import org.example.entity.Artist;

import java.util.List;

public class AlbumRepositoryImpl implements AlbumRepository {

    private final EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();

    @Override
    public boolean existsByUniqueId(Album album) {
        return emf.callInTransaction(em ->
            em.createQuery("select count(a) from Album a where a.albumId = :albumId", Long.class)
                .setParameter("albumId", album.getAlbumId())
                .getSingleResult() > 0
        );
    }

    @Override
    public void save(Album album) {
        emf.runInTransaction(em -> em.persist(album));
    }

    @Override
    public List<Album> findAll() {
        return emf.callInTransaction(em ->
            em.createQuery("select a from Album a", Album.class)
                .getResultList());
    }

    @Override
    public List<Album> findByArtist(Artist artist) {
        return emf.callInTransaction(em ->
            em.createQuery("select a from Album a where a.artist = :artist", Album.class)
                .setParameter("artist", artist)
                .getResultList()
        );
    }

    @Override
    public List<Album> findByGenre(String genre) {
        return emf.callInTransaction(em ->
            em.createQuery("select a from Album a where a.genre = :genre", Album.class)
                .setParameter("genre", genre)
                .getResultList()
        );
    }

    @Override
    public Long count() {
        return emf.callInTransaction(em ->
            em.createQuery("select count(a) from Album a", Long.class)
                .getSingleResult());
    }
}
