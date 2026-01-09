package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@jakarta.persistence.Table(name = "Guests")

public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Name", nullable = false)
    private String name;

    @Column(name="Note", nullable = false)
    private String note;

    @Column(name="Contact_info", nullable = false)
    private String contact;

    @ManyToMany(mappedBy = "guests")
    private List<Booking> bookings = new ArrayList<>();

    public Guest(String name, String note, String contact){
        this.name = name;
        this.note = note;
        this.contact = contact;
    }

    public Guest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
