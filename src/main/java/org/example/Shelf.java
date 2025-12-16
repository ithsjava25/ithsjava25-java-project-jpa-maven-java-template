package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Shelf {

    @Id
    private Long shelfId;
    private String name;
    private String genre;
    
    public Shelf(){
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
