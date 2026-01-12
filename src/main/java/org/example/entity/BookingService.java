package org.example.service;

import jakarta.persistence.EntityManagerFactory;
import org.example.entity.*;

import java.time.LocalDate;
import java.util.List;

public class BookingService {

    private final EntityManagerFactory emf;

    public BookingService(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Skapa gäst
    public Long createGuest(String name, String note, String contact) {
        return emf.callInTransaction(em -> {
            Guest guest = new Guest(name, note, contact);
            em.persist(guest);
            em.flush();
            return guest.getId();
        });
    }

    // Skapa bokning MED validering
    public void createBooking(Long tableId, Long timeSlotId, LocalDate date, int partySize, List<Long> guestIds) {
        emf.runInTransaction(em -> {
            // 1. Hämta bord
            Table table = em.find(Table.class, tableId);
            if (table == null) {
                throw new IllegalArgumentException("Table not found!");
            }

            // 2. Hämta tidslucka
            TimeSlot timeSlot = em.find(TimeSlot.class, timeSlotId);
            if (timeSlot == null) {
                throw new IllegalArgumentException("TimeSlot not found!");
            }

            // 3. VALIDERA KAPACITET
            if (partySize > table.getCapacity()) {
                throw new IllegalArgumentException(
                    "Party size (" + partySize + ") exceeds table capacity (" + table.getCapacity() + ")!"
                );
            }

            if (partySize < 1) {
                throw new IllegalArgumentException("Party size must be at least 1!");
            }

            // 4. VALIDERA DATUM
            LocalDate today = LocalDate.now();
            LocalDate maxDate = today.plusMonths(3);

            if (date.isBefore(today)) {
                throw new IllegalArgumentException("Cannot book a date in the past!");
            }

            if (date.isAfter(maxDate)) {
                throw new IllegalArgumentException("Cannot book more than 3 months in advance!");
            }

            // 5. VALIDERA ATT BORDET INTE ÄR BOKAT FÖR SAMMA TID/DATUM
            Long existingBookings = em.createQuery(
                    "SELECT COUNT(b) FROM Booking b " +
                        "WHERE b.table.id = :tableId " +
                        "AND b.date = :date " +
                        "AND b.timeSlot.id = :timeSlotId " +
                        "AND b.status != 'CANCELLED'",
                    Long.class
                )
                .setParameter("tableId", tableId)
                .setParameter("date", date)
                .setParameter("timeSlotId", timeSlotId)
                .getSingleResult();

            if (existingBookings > 0) {
                throw new IllegalArgumentException(
                    "Table " + table.getTableNumber() +
                        " is already booked for " + date +
                        " at " + timeSlot.getStartTime() + "!"
                );
            }

            // 6. Validera att minst en gäst finns
            if (guestIds == null || guestIds.isEmpty()) {
                throw new IllegalArgumentException("Booking must have at least one guest!");
            }

            // 7. Skapa bokning
            Booking booking = new Booking();
            booking.setDate(date);
            booking.setTimeSlot(timeSlot);
            booking.setParty(partySize);
            booking.setTable(table);

            // 8. Lägg till gäster
            for (Long guestId : guestIds) {
                Guest guest = em.find(Guest.class, guestId);
                if (guest != null) {
                    booking.addGuest(guest);
                } else {
                    throw new IllegalArgumentException("Guest with ID " + guestId + " not found!");
                }
            }

            em.persist(booking);
            System.out.println("Booking created successfully!");
        });
    }

    public List<Table> getAllTables() {
        return emf.callInTransaction(em ->
            em.createQuery("SELECT t FROM Table t", Table.class).getResultList()
        );
    }

    public List<TimeSlot> getAllTimeSlots() {
        return emf.callInTransaction(em ->
            em.createQuery("SELECT ts FROM TimeSlot ts", TimeSlot.class).getResultList()
        );
    }

    public List<Guest> getAllGuests() {
        return emf.callInTransaction(em ->
            em.createQuery("SELECT g FROM Guest g", Guest.class).getResultList()
        );
    }

    public List<Booking> getAllBookings() {
        return emf.callInTransaction(em ->
            em.createQuery(
                "SELECT DISTINCT b FROM Booking b " +
                    "LEFT JOIN FETCH b.guests " +
                    "LEFT JOIN FETCH b.table " +
                    "LEFT JOIN FETCH b.timeSlot",
                Booking.class
            ).getResultList()
        );
    }

    public void updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        emf.runInTransaction(em -> {
            Booking booking = em.find(Booking.class, bookingId);
            if (booking == null) {
                System.out.println("Booking with ID " + bookingId + " not found!");
                return;
            }

            switch (newStatus) {
                case CONFIRMED -> booking.confirmBooking();
                case CANCELLED -> booking.cancelBooking();
                case COMPLETED -> booking.completeBooking();
                case NO_SHOW -> booking.noShowBooking();
            }

            System.out.println("Booking status updated to: " + newStatus);
        });
    }

    public void deleteBooking(Long bookingId) {
        emf.runInTransaction(em -> {
            try {
                Booking booking = em.createQuery(
                        "SELECT b FROM Booking b " +
                            "LEFT JOIN FETCH b.guests " +
                            "WHERE b.id = :id",
                        Booking.class
                    )
                    .setParameter("id", bookingId)
                    .getSingleResult();

                em.remove(booking);
                System.out.println("Booking deleted successfully!");

            } catch (jakarta.persistence.NoResultException e) {
                System.out.println("Booking with ID " + bookingId + " not found!");
            }
        });
    }
}
