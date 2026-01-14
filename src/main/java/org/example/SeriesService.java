package org.example;

public class SeriesService {

    private final SeriesRepository<Series> seriesRepository;

    public SeriesService(SeriesRepository<Series> seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public Series create(Series series) {
        return seriesRepository.save(series);
    }

//    public void addSeries(Long directorId, Series series) {
//        Director director = directorRepository.findById(directorId)
//            .orElseThrow(() -> new RuntimeException("Director not found: " + directorId));
//
//        director.addSeries(series);
//        directorRepository.save(director);
//    }
    public void update(Series series) {
        seriesRepository.save(series);
    }

    public Iterable<Series> findAll() {
        return seriesRepository.findAll();

    }

    public Series findSeriesId(Long id) {
        return seriesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Series not found: " + id));
    }

    public Series findSeriesTitle(String title) {
        return seriesRepository.findByTitle(title)
            .orElseThrow(() -> new RuntimeException("Series not found: " + title));
    }
}
