import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

import java.util.List;

public class Main {
        public static void main(String[] args) {


            final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
                .jdbcUrl("jdbc:mysql://localhost:3306/film_database")
                .jdbcUsername("root")
                .jdbcPassword("root")
                .property("hibernate.hbm2ddl.auto", "update")
                .property("hibernate.show_sql", "true")
                .property("hibernate.format_sql", "true")
                .property("hibernate.highlight_sql", "true")
                .managedClasses(Film.class, Director.class);
            try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {
                emf.runInTransaction(em -> {
                    //If no Films in database, add some
                    if (em.createQuery("select count(o) from Film o", Long.class)
                        .getSingleResult() == 0) {
                        Film film1 = new Film();
                        em.persist(film1);
                        em.flush();
                        Film film2 = new Film();
                        em.persist(film2);
                    }
                    System.out.println("==== Using select query, N + 1 ====");
                    em.createQuery("from Film", Film.class)
                        .getResultList().forEach(System.out::println);

                });

            }

        }
}
