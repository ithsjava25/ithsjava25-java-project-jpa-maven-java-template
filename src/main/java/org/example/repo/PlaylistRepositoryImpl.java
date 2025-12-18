package org.example.repo;

import org.example.entity.Playlist;
import org.example.entity.Song;

import java.util.List;

public class PlaylistRepositoryImpl implements PlaylistRepository {

    @Override
    public List<Playlist> findAll() {
        return List.of();
    }

    @Override
    public List<Song> findSongsInPlaylist(Playlist playlist) {
        return List.of();
    }

    @Override
    public void createPlaylist(String name) {

    }

    @Override
    public void deletePlaylist(Playlist playlist) {

    }

    @Override
    public void addSong(Playlist playlist, Song song) {

    }

    @Override
    public void removeSong(Playlist playlist, Song song) {

    }

    @Override
    public void renamePlaylist(Playlist playlist, String newName) {

    }
}
