package org.example;

import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Song;
import org.example.repo.AlbumRepository;
import org.example.repo.ArtistRepository;
import org.example.repo.SongRepository;

import java.util.List;

public class DatabaseInitializer {

    private final ItunesApiClient apiClient;

    private final SongRepository songRepo;
    private final AlbumRepository albumRepo;
    private final ArtistRepository artistRepo;

    public DatabaseInitializer(ItunesApiClient apiClient, SongRepository songRepo , AlbumRepository albumRepo, ArtistRepository artistRepo) {
        this.apiClient = apiClient;
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.artistRepo = artistRepo;
    }

    public void init() {
        if (songRepo.count() > 0) { //check if there is data already
            return;
        }

        List<String> searches = List.of("the+war+on+drugs",
            "refused",
            "thrice",
            "16+horsepower",
            "viagra+boys",
            "geese",
            "ghost",
            "run+the+jewels",
            "rammstein",
            "salvatore+ganacci",
            "baroness"
        );
        for (String term : searches) {
            try {
                apiClient.searchSongs(term).forEach(dto -> {
                    Artist ar = Artist.fromDTO(dto);
                    if (!artistRepo.existsByUniqueId(ar)) {
                        artistRepo.save(ar);
                    }

                    Album al = Album.fromDTO(dto, ar);
                    if (!albumRepo.existsByUniqueId(al)) {
                        albumRepo.save(al);
                    }

                    Song s = Song.fromDTO(dto, al);
                    if (!songRepo.existsByUniqueId(s)) {
                        songRepo.save(s);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch or persist data for search term: " + term, e);
            }
        }
    }
}
