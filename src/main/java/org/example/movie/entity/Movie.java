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

    @Column(name = "image_url")
    private String imageUrl;

    private String releaseYear;
    private Double imdbRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();

    protected Movie() {}

    public Movie(String title) {
        this.title = title;
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

    public String getReleaseYear() { return releaseYear; }
    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public List<Role> getRoles() { return roles; }

    public void addRole(Role role) {
        roles.add(role);
        role.setMovie(this);
    }
}
