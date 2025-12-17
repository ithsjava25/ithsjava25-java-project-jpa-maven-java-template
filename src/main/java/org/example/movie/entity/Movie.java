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

    private Integer releaseYear;
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

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public List<Role> getRoles() { return roles; }
}
