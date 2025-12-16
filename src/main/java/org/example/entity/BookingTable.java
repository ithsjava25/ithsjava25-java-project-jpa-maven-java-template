package org.example.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "tables")

public class BookingTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="table_number", nullable = false, unique = true)
    private int tableNumber;

    @Column(name="Capacity", nullable = false)
    private int capacity;

    public BookingTable( int capacity, int tableNumber){
        this.capacity = capacity;
        this.tableNumber = tableNumber;
    }

    public BookingTable() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
