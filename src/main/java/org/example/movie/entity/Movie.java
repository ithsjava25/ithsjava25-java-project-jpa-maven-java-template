package org.example.movie.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String genre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, unique = true)
    private Integer tmdbId;

    @Column(name = "image_url")
    private String imageUrl;

    private Integer releaseYear;
    private Double imdbRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    protected Movie() {}

    public Movie(String title, Integer tmdbId) {
        this.title = title;
        this.tmdbId = tmdbId;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        if (tmdbId == null || tmdbId <= 0) {
            throw new IllegalArgumentException("tmdbId must be a positive number");
        }
        this.tmdbId = tmdbId;
    }

    public List<Role> getRoles() { return roles; }

    public void addRole(Role role) {
        roles.add(role);
        role.setMovie(this);
    }
}
