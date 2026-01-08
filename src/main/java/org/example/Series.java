package org.example;

import jakarta.persistence.*;
import org.example.Director;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Series extends BaseEntity{


    private Long id;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Director> directors = new HashSet<>();

    private String title;
    private int episodes;
    private int firstAired;
    private Integer lastAired;
    private String starActors;

    public void setId(Long id) {

        this.id = id;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setDirectors(Set<Director> directors) {

        this.directors = directors;
    }

    public void setEpisodes(int episodes) {

        this.episodes = episodes;
    }

    public void setFirstAired(int firstAired) {

        this.firstAired = firstAired;
    }

    public void setLastAired(Integer lastAired) {

        this.lastAired = lastAired;
    }

    public void setStarActors(String starActors) {

        this.starActors = starActors;
    }



    private static void add(Series s) {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<Director> getDirectors() {
        return directors;
    }

    public int getEpisodes() {
        return episodes;
    }

    public int getFirstAired() {
        return firstAired;
    }

    public Integer getLastAired() {
        return lastAired;
    }

    public String getStarActors() {
        return starActors;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Series)) return false;
//        Series other = (Series) o;
//        return id != null && id.equals(other.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return 31;
//    }
}
