package org.example.model;

import javax.persistence.*;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int duration; // i sekunder

    @ManyToOne
    private Album album;

    public Song() {}

    public Song(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    // Get & Set
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
}
