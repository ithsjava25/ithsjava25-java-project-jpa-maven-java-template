package org.example;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.persistence.*;
import org.example.entity.*;
import org.example.entity.Table;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

import java.util.List;

public class App {
    static void main(String[] args) {

        List<Class<?>> entities = getEntities("org.example.entity");

        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/restaurant_booking")
            .jdbcUsername("root")
            .jdbcPassword("root123")

            .property("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")

            .property("hibernate.hikari.maximumPoolSize", "10") //Max antal anslutningar
            .property("hibernate.hikari.minimumIdle", "5") //Minsta antal lediga anslutningar
            .property("hibernate.hikari.idleTimeout", "300000") // 5 minuter innan ledig anslutning stängs
            .property("hibernate.hikari.connectionTimeout", "20000") // Max väntetid på anslutning (20s)

            .property("hibernate.hbm2ddl.auto", "create")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(entities);

        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {

            hours(emf);
            createTable(emf);
            createGuest(emf);


            //Show all the hours available
            emf.runInTransaction(em -> {
                var query = em.createQuery("select h from TimeSlot h");
                query.getResultList().forEach(System.out::println);
            });

        }

    }

    private static void createGuest (EntityManagerFactory emf){
        emf.runInTransaction(em -> {

            Guest guest = new Guest("Gabriela", "Bord för fyra", "072762668");
            em.persist(guest);

            guest = new Guest("Samuel", "Bord för 3", "072778882");
            em.persist(guest);

        });
    }

    private static void createTable(EntityManagerFactory emf) {
        emf.runInTransaction(em -> {

            Table table = new Table();
            table.setTableNumber("1");
            table.setCapacity(4);
            em.persist(table);

            table = new Table();
            table.setTableNumber("2");
            table.setCapacity(2);
            em.persist(table);

            table = new Table();
            table.setTableNumber("3");
            table.setCapacity(3);
            em.persist(table);

            table = new Table();
            table.setTableNumber("4");
            table.setCapacity(6);
            em.persist(table);

            table = new Table();
            table.setTableNumber("5");
            table.setCapacity(2);
            em.persist(table);

        });
    }

    private static void hours(EntityManagerFactory emf){
        emf.runInTransaction(em -> {

            TimeSlot hour = new TimeSlot("16:00", "18:00");
            em.persist(hour);

            hour = new TimeSlot("16:30", "18:30");
            em.persist(hour);

            hour = new TimeSlot("17:00", "19:00");
            em.persist(hour);

            hour = new TimeSlot("17:30", "19:30");
            em.persist(hour);

            hour = new TimeSlot("18:00", "20:00");
            em.persist(hour);

            hour = new TimeSlot("18:30", "20:30");
            em.persist(hour);

            hour = new TimeSlot("19:00", "21:00");
            em.persist(hour);

        });
    }

    private static List<Class<?>> getEntities(String pkg) {
        List<Class<?>> entities;

        //Esto es para scannear todas las entities clases que existen en tal paquete. Es para no hacerlo uno por uno
        //Agregar dependency

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
