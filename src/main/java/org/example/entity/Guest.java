package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Guests")

public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="booking_id", nullable = false)
    private Booking bookingId;

    @Column(name="Name", nullable = false)
    private String name;

    @Column(name="Note", nullable = false)
    private String note;

    @Column(name="Contact_info", nullable = false)
    private String contact;

    public Guest(String name, String note, String contact){
        this.name = name;
        this.note = note;
        this.contact = contact;
    }

    public Guest(){}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
