package org.example.repo;

import org.example.entity.Artist;

import java.util.List;

public interface ArtistRepository {

    boolean existsByUniqueId(Artist artist);

    void save(Artist artist);

    List<Artist> findAll();

    Long count();

}
