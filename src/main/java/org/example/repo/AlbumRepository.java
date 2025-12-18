package org.example.repo;

import org.example.entity.Album;
import org.example.entity.Artist;

import java.util.List;

public interface AlbumRepository {

    boolean existsByUniqueId(Album album);

    void save(Album album);

    List<Album> findAll();

    List<Album> findByArtist(Artist artist);

    List<Album> findByGenre(String genre);

    Long count();
}
