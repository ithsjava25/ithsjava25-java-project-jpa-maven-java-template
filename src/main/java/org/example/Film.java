package org.example;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.example.Director;

import java.util.Objects;

@Entity
public class Film extends BaseEntity{


    private Long id;

    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    @ManyToOne

    @NotNull(message = "A film must have a director")
    private Director director;

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {

        return id;
    }








    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film other = (Film) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
