package org.example;

public class FilmService {

    private final FilmRepository<Film> filmRepository;

    public FilmService(FilmRepository<Film> filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film create(Film film) {
        return filmRepository.save(film);
    }

    public void update(Film film) {
        filmRepository.save(film);
    }

    public FilmDTO find(Long id) {
        return filmRepository.findById(id)
            .map(f -> new FilmDTO(f.getTitle()))
            .orElseThrow(() -> new RuntimeException("Film not found: " + id));
    }

    public Film findFilm(Long id) {
        return filmRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film not found: " + id));
    }
}
