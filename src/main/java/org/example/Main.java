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

            // 2. The Service logic happens inside a transaction
            emf.runInTransaction(em -> {

                DirectorRepositoryImpl directorRepository = new DirectorRepositoryImpl(emf);
                Director d = new Director();
                try {
                    em.getTransaction().begin();
                    em.getTransaction().commit();

                    em.close();
                    emf.close();






                } catch (RuntimeException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            });

        }
    }




}
