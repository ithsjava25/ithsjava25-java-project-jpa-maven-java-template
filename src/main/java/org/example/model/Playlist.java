package org.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<PlaylistSong> entries = new ArrayList<>();

    public Playlist() {}

    public Playlist(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public void addSong(Song song, int position) {
        PlaylistSong entry = new PlaylistSong(this, song, position);
        entries.add(entry);
    }

    public void removeSongBySongId(Long songId) {
        entries.removeIf(entry -> songId.equals(entry.getSong().getId()));
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<PlaylistSong> getEntries() { return entries; }

    public void setName(String name) { this.name = name; }
}
