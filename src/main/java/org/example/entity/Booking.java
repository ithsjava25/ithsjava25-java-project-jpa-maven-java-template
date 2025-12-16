package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="bookings")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
//    @JoinColumn(name="table_id", nullable = false)
    private BookingTable tableId;

//    @ManyToMany(mappedBy = "guest")
//    @JoinTable(name="guest_id",
//        joinColumns = @JoinColumn(name = "booking_id"),
//        inverseJoinColumns = @JoinColumn(name= "guest_id"))
//    private Guest guestId;

    @Column(name="Booking_Time", nullable = false)
    private LocalDateTime time;

    @Column(name="Party", nullable = false)
    private int party;

    public Booking(int tableId, LocalDateTime time, int party){
        //this.tableId = tableId;
        this.time = time;
        this. party = party;
    }

    public Booking() {}


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
