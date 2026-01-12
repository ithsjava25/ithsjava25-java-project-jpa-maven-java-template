package org.example;

public record FilmDTO(String title) {
    public FilmDTO(Film film) {
        this(film.getTitle());
    }
}
