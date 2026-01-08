package org.example;


import java.util.Set;

public record DirectorDTO(String name, String country) {
    public DirectorDTO(Director director) {
        this(director.getName(), director.getCountry());
//        Long id;
//        String name;
//        String country;
//        int birthYear;
//        Integer yearOfDeath;
//        Set<Long> filmIds;
//        Set<Long> seriesIds;
    }
}
