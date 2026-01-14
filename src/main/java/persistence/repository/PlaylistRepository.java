package persistence.repository;

import org.example.model.Playlist;
import org.example.model.Song;
import org.example.model.PlaylistSong;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PlaylistRepository {
    private final EntityManager em;

    public PlaylistRepository(EntityManager em) {
        this.em = em;
    }

    public Playlist createPlaylist(String name) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Playlist p = new Playlist(name);
            em.persist(p);
            transaction.commit();
            return p;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public List<Playlist> findAll() {
        return em.createQuery("SELECT p FROM Playlist p ORDER BY p.createdAt DESC", Playlist.class)
            .getResultList();
    }

    public void addSong(Long playlistId, Long songId, int position) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Playlist playlist = em.find(Playlist.class, playlistId);
            if (playlist == null) throw new IllegalArgumentException("Playlist not found.");

            Song song = em.find(Song.class, songId);
            if (song == null) throw new IllegalArgumentException("Song not found.");

            PlaylistSong entry = new PlaylistSong(playlist, song, position);
            em.persist(entry);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public void removeSong(Long playlistId, Long songId) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Playlist playlist = em.find(Playlist.class, playlistId);
            if (playlist == null) throw new IllegalArgumentException("Playlist not found.");

            // Hitta PlaylistSong-attributet
            PlaylistSong entry = em.createQuery(
                    "SELECT ps FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId AND ps.song.id = :songId",
                    PlaylistSong.class)
                .setParameter("playlistId", playlistId)
                .setParameter("songId", songId)
                .getResultStream()
                .findFirst()
                .orElse(null);

            if (entry != null) {
                em.remove(entry);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }
}
