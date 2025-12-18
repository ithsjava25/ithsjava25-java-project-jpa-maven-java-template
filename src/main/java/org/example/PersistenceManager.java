package org.example;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

import java.util.List;

public class PersistenceManager {

    private static final EntityManagerFactory emf;

    static {

        List<Class<?>> entities = getEntities("org.example.entity");

        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/myPodDB")
            .jdbcUsername("user")
            .jdbcPassword("pass")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(entities);

        emf = cfg.createEntityManagerFactory();

        //From CodeRabbit: Register a shutdown hook to properly release database connections on JVM exit.
        Runtime.getRuntime().addShutdownHook(new Thread(emf::close));

    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    //Not invented here!!
    private static List<Class<?>> getEntities(String pkg) {
        List<Class<?>> entities;
        try (ScanResult scanResult =
                 new ClassGraph()
                     .enableClassInfo()
                     .enableAnnotationInfo()
                     .acceptPackages(pkg)
                     .scan()) {
            entities = scanResult.getClassesWithAnnotation(Entity.class).loadClasses();
        }
        return entities;
    }
}
