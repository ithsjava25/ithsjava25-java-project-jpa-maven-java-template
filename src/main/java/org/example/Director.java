package org.example;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Director extends BaseEntity{


    //private Long id;

    @OneToMany(
        mappedBy = "director",
        cascade = CascadeType.PERSIST,
        orphanRemoval = true

    )
    private Set<Film> films = new HashSet<>();

    @ManyToMany
    @JoinTable(
                name = "director_series",
                joinColumns = @JoinColumn(name = "director_id"),
                inverseJoinColumns = @JoinColumn(name = "series_id"))
    private Set<Series> series = new HashSet<>();

    private String name;

    private String country;

    private Integer birthYear;


    private Integer yearOfDeath;



    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

    public Integer getYearOfDeath() {
        return yearOfDeath;
    }

    public void setYearOfDeath(Integer yearOfDeath) {
        this.yearOfDeath = yearOfDeath;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void addFilm(Film film) {
        films.add(film);
        film.setDirector(this);

    }

    public void removeFilm(Film film) {
        films.remove(film);
        film.setDirector(null);
    }



    public void addSeries(Series s) {
        series.add(s);
        s.getDirectors().add(this);
    }
    public void removeSeries(Series s) {
        s.getDirectors().remove(this);
        series.remove(s);
    }



}
