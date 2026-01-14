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

    public Iterable<Film> findAll() {
        return filmRepository.findAll();

    }

    public Film findFilmId(Long id) {
        return filmRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film not found: " + id));
    }

    public Film findFilmTitle(String title) {
        return filmRepository.findByTitle(title)
            .orElseThrow(() -> new RuntimeException("Film not found: " + title));
    }
}
