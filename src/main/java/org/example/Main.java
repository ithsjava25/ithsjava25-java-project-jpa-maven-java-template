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

            //Old code that CodeRabbit suggested to remove
//                try {
//                    em.getTransaction().begin();
//                    DirectorRepositoryImpl directorRepository = new DirectorRepositoryImpl(emf);
//                    Director d = new Director();
//                    d.setName("John Doe");
//
//                    directorRepository.save(d);
//
//                    em.getTransaction().commit();
//
//                } catch (Exception e) {
//                    System.err.println("Error: " + e.getMessage());
//
//                } finally {
//                    em.close();
//                }
                Director d = new Director();
                d.setName("Example Director");
                d.setCountry("USA");
                d.setBirthYear(1970);
                em.persist(d);
            });
        }
    }
}
