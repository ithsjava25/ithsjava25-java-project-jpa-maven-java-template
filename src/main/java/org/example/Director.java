package org.example;

import jakarta.persistence.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Director extends BaseEntity{


    private Long id;

    @OneToMany(
        mappedBy = "director",
        cascade = CascadeType.PERSIST,
        orphanRemoval = true

    )
    private Set<Film> films = new HashSet<>();

    @ManyToMany()
    private Set<Series> series = new HashSet<>();

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    private String country;

    @Min(1850)
    private int birthYear;

    @Column(nullable = true)
    @Max(2100)
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

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
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

    public void setId(Long id) {

        this.id = id;
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

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Director)) return false;
        Director other = (Director) o;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() {
       return 31;
    }
}
