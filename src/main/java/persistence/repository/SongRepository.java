package persistence.repository;

import org.example.model.Song;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class SongRepository {
    private final EntityManager em;

    public SongRepository(EntityManager em) {
        this.em = em;
    }

    public Song save(Song song) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(song);
            transaction.commit();
            return song;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public Song findById(Long id) {
        return em.find(Song.class, id);
    }

    public List<Song> findAll() {
        return em.createQuery("SELECT s FROM Song s", Song.class).getResultList();
    }

    public List<Song> findByAlbumId(Long albumId) {
        return em.createQuery("SELECT s FROM Song s WHERE s.album.id = :albumId", Song.class)
            .setParameter("albumId", albumId)
            .getResultList();
    }

    public boolean deleteSong(Long id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Song song = em.find(Song.class, id);
            if (song != null) {
                em.remove(song);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            return false;
        }
    }
}
