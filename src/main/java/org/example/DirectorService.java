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
        Director director = directorRepository.findById(directorId)
            .orElseThrow(() -> new RuntimeException("Director not found: " + directorId));

        director.addFilm(film);
        directorRepository.save(director);
    }

    public Director update(Long id, Director director) {
        Director existingDirector = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Director not found"));


        existingDirector.setYearOfDeath(director.getYearOfDeath());
        existingDirector.setCountry(director.getCountry());
        existingDirector.setBirthYear(director.getBirthYear());
        existingDirector.setName(director.getName());

        for (Film film : director.getFilms()) {
            existingDirector.addFilm(film);
        }
        return directorRepository.save(existingDirector);
    }


    public Iterable<Director> findAll() {
        return directorRepository.findAll();

    }


    public void delete(Long id) {
        Director director = directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Director not found: " + id));

        directorRepository.delete(director);
    }

    public Director findDirector(Long id) {
        //Old code
//        Director director = (Director) directorRepository.findById(id)
//            .orElseThrow();
//
//        return director;
        //New code
        return directorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Director not found: " + id));
    }
}
    //    public DirectorDTO find(Long id) {
//        return directorRepository.findById(id)
//            .map(d -> new DirectorDTO(d.getName(), d.getCountry()))
//            .orElseThrow(() -> new RuntimeException("Director not found"));
//    }
