package persistence.repository;

import org.example.model.Playlist;
import org.example.model.Song;
import javax.persistence.EntityManager;
import java.util.List;

public class PlaylistRepository {
    private final EntityManager em;

    public PlaylistRepository(EntityManager em) {
        this.em = em;
    }

    public Playlist createPlaylist(String name) {
        Playlist p = new Playlist(name);
        em.persist(p);
        return p;
    }

    public List<Playlist> findAll() {
        return em.createQuery("SELECT p FROM Playlist p ORDER BY p.createdAt DESC", Playlist.class)
            .getResultList();
    }

    public void addSong(Long playlistId, Long songId, int position) {
        Playlist playlist = em.find(Playlist.class, playlistId);
        if (playlist == null) throw new IllegalArgumentException("Playlist not found.");

        Song song = em.find(Song.class, songId);
        if (song == null) throw new IllegalArgumentException("Song not found.");

        playlist.addSong(song, position);
    }

    public void removeSong(Long playlistId, Long songId) {
        Playlist playlist = em.find(Playlist.class, playlistId);
        if (playlist == null) throw new IllegalArgumentException("Playlist not found.");

        playlist.removeSongBySongId(songId);
    }
}
