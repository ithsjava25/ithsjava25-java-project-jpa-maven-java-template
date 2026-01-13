package persistence.repository;

import org.example.model.Song;
import javax.persistence.EntityManager;
import java.util.List;

public class SongRepository {
    private final EntityManager em;

    public SongRepository(EntityManager em) {
        this.em = em;
    }

    public Song save(Song song) {
        em.persist(song);
        return song;
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
        try {
            Song song = findById(id);
            if (song != null) {
                em.remove(song);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
