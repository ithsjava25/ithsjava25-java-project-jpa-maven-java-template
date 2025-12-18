package org.example.repo;

import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Song;

import java.util.List;

public interface SongRepository {
    Long count();

    boolean existsByUniqueId(Song song);

    void save(Song song);

    List<Song> findAll();

    List<Song> findByArtist(Artist artist);

    // Redundant?
    List<Song> findByAlbum(Album album);

    List<Song> findByGenre(String genre);

}
