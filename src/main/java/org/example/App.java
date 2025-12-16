package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.Booking;
import org.example.entity.Table;
import org.example.entity.Guest;

import java.time.LocalDateTime;

public class App {
    static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("restaurantPU");
        EntityManager em = emf.createEntityManager();

        try{
            em.getTransaction().begin();
            //BookingTable bookingTable = new BookingTable(6, 8);
            Booking booking = new Booking(2L ,LocalDateTime.now(), 6);
            //Guest guest = new Guest();
            em.persist(booking);
            em.getTransaction().commit();
        } catch (Exception e){
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
