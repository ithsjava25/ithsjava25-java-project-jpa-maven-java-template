package org.example.repo;

import org.example.entity.Album;
import org.example.entity.Artist;

import java.util.List;

public interface AlbumRepository {

    boolean existsByUniqueId(Album album);

    void save(Album album);

    Long count();

    List<Album> findAll();

    Album findByArtist(Artist artist);

    Album findByGenre(String genre);
}
