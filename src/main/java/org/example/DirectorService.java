package org.example;



public class DirectorService {

    private final DirectorRepository<Director> directorRepository;

    public DirectorService(DirectorRepository<Director> directorRepository) {
        this.directorRepository = directorRepository;
    }


    public Director create(Director director) {
        return directorRepository.save(director);
    }


    public void addFilm(Long directorId, Film film) {
        Director director =  directorRepository.findById(directorId)
            .orElseThrow();

        director.addFilm(film);
          directorRepository.save(director);
    }

    public DirectorDTO find(Long id) {
        return directorRepository.findById(id)
            .map(d -> new DirectorDTO(d.getName(), d.getCountry()))
            .orElseThrow(() -> new RuntimeException("D irector not found"));
    }

    public void delete(Long id) {
        Director director = directorRepository.findById(id)
            .orElseThrow();

        directorRepository.delete(director);
    }

    public Director findDirector(Long id) {
        Director director = (Director) directorRepository.findById(id)
            .orElseThrow();

        return director;

    }
}
