package org.example;

import jakarta.persistence.*;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

public class Main {
     public static void main(String[] args) {


        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/testdb")
            .jdbcUsername("user")
            .jdbcPassword("password")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(Film.class, Director.class, Series.class);

         try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {
             emf.runInTransaction(em -> {
                 DirectorRepositoryImpl directorRepository = new DirectorRepositoryImpl(em);
                 if (!directorRepository.findAll().iterator().hasNext()) {
                     Director d = new Director();
                     d.setName("John Doe");
                     directorRepository.save(d);
                 }
             });

             DirectorRepositoryImpl directorRepository = new DirectorRepositoryImpl(em);
             DirectorService directorService = new DirectorService(directorRepository);

             FilmRepositoryImpl filmRepository = new FilmRepositoryImpl(em);
             FilmService filmService = new FilmService(filmRepository);

             SeriesRepositoryImpl seriesRepository = new SeriesRepositoryImpl(emf);
             SeriesService seriesService = new SeriesService(seriesRepository);

             CLI cli = new CLI();
             cli.cliStart(directorService, filmService, seriesService);
    }
}
