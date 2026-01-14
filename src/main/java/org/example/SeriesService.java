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

    public SeriesDTO find(Long id) {
        return seriesRepository.findById(id)
            .map(SeriesDTO::new)
            .orElseThrow(() -> new RuntimeException("Series not found: " + id));
    }

    public Series findSeries(Long id) {
        return seriesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Series not found: " + id));
    }
}
