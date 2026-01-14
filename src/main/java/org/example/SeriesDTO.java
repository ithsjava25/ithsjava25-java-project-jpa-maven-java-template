package org.example;

public record SeriesDTO(String title, int episodes, int firstAired, Integer lastAired, String starActors){
    public SeriesDTO(Series series) {
        this(series.getTitle(),
            series.getEpisodes(),
            series.getFirstAired(),
            series.getLastAired(),
            series.getStarActors());
    }
}
