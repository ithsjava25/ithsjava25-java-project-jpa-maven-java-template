package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name="Bookings")

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="booking_date", nullable = false)
    private LocalDate date; //day of the reservation

    @ManyToOne
    @JoinColumn(name="timeslot_id")
    private TimeSlot timeSlot; //time selected from the determited times

    @Column(name="party_size", nullable = false)
    private int partySize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @ManyToOne
    @JoinColumn(name="table_id", nullable = false)
    private Table table;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "booking_guests",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn (name = "guest_id"))
    private List<Guest> guests = new ArrayList<>();

    public Booking(Long id, LocalDate date, TimeSlot timeSlot, int partySize, Table table, List<Guest> guests) {
        this.id = id;
        this.date = date;
        this.timeSlot = timeSlot;
        this.partySize = partySize;
        this.table = table;
        this.guests = guests;
        this.status = BookingStatus.PENDING;

    }

    public void addGuest(Guest guest){
        guests.add(guest);
        guest.getBookings().add(this);
    }

    public void removeGuest(Guest guest){
        guests.remove(guest);
        guest.getBookings().remove(this);
    }

    public BookingStatus getStatus() {
        return status;
    }
    public void confirmBooking(){
        this.status = BookingStatus.CONFIRMED;
    }
    public void cancelBooking(){
        this.status = BookingStatus.CANCELLED;
    }
    public void completeBooking(){
        this.status = BookingStatus.COMPLETED;
    }
    public void noShowBooking(){
        this.status = BookingStatus.NO_SHOW;
    }

    public void setStatus(BookingStatus status) {}

    public Booking() {}

    public LocalDate getDate() {
        return date;
    }

    public void setTime(LocalDate time) {
        this.date = date;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getParty() {
        return partySize;
    }

    public void setParty(int party) {
        this.partySize = partySize;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + id +
            ", date=" + date +
            ", timeSlot=" + timeSlot +
            ", party=" + partySize +
            ", table=" + table +
            ", guests=" + guests +
            '}';
    }
}
