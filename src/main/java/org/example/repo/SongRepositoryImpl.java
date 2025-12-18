package org.example.repo;

import jakarta.persistence.EntityManagerFactory;
import org.example.PersistenceManager;
import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Song;

import java.util.List;

public class SongRepositoryImpl implements SongRepository {

    private final EntityManagerFactory emf = PersistenceManager.getEntityManagerFactory();


    @Override
    public Long count() {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("select count(s) from Song s", Long.class)
                .getSingleResult();
        }
    }

    @Override
    public boolean existsByUniqueId(Song song) {
        try (var em = emf.createEntityManager()) {
            return em.createQuery("select count(s) from Song s where s.songId = :songId", Long.class)
                .setParameter("songId", song.getSongId())
                .getSingleResult() > 0;
        }
    }


    @Override
    public void save(Song song) {
        emf.runInTransaction(em -> em.persist(song));
    }

    @Override
    public List<Song> findAll() {
        return List.of();
    }

    @Override
    public List<Song> findByArtist(Artist artist) {
        return List.of();
    }

    @Override
    public List<Song> findByAlbum(Album album) {
        return List.of();
    }

    @Override
    public List<Song> findByGenre(String genre) {
        return List.of();
    }


}
