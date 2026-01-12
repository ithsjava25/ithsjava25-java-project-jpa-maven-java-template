package org.example.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@jakarta.persistence.Table(name = "Tables")

public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @OneToMany(mappedBy = "table", cascade = CascadeType.PERSIST)
   private List<Booking> bookings = new ArrayList<>();


    @Column(name="table_number", nullable = false, unique = true)
    private String tableNumber;

    @Column(name="capacity", nullable = false)
    private int capacity;


    public Table(int capacity, String tableNumber){
        this.capacity = capacity;
        this.tableNumber = tableNumber;

    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Table() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
