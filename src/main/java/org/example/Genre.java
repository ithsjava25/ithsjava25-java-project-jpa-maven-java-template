package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;
    private String genreName;

    public Genre(){
    }

    public String getGenre() {
        return genreName;
    }

    public void setGenre(String genre) {
        this.genreName = genre;
    }

    public Long getGenreId() {
        return genreId;
    }

    @ManyToMany @JoinTable(name = "book_genre",
        joinColumns = @JoinColumn(name="genreId"))
    private List<Book> books = new ArrayList<>();

}
