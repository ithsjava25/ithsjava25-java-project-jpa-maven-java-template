package org.example.repo;

import org.example.entity.Playlist;
import org.example.entity.Song;

import java.util.List;

public interface PlaylistRepository {

    List<Playlist> findAll();

    List<Song> findSongsInPlaylist(Playlist playlist);

    void createPlaylist(String name);

    void deletePlaylist(Playlist playlist);

    void addSong(Playlist playlist, Song song);

    void removeSong(Playlist playlist, Song song);

    void renamePlaylist(Playlist playlist, String newName);
}
