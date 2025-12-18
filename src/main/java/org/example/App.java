package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.Booking;
import org.example.entity.Guest;
import org.example.entity.RestaurantTable;

import java.time.LocalDateTime;

public class App {
    static void main(String[] args) {

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("restaurantPU");
             EntityManager em = emf.createEntityManager()) {

            try {
                em.getTransaction().begin();
                RestaurantTable table = new RestaurantTable(8, "Table 8");
                em.persist(table);
                Guest guest = new Guest("John Smith", "N/A", "johnsmith.email.com");
                em.persist(guest);
                Booking booking = new Booking(table, LocalDateTime.now(), 5);
                em.persist(booking);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
