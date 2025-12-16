package org.example.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Tables")

public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Table table;

   @OneToMany(mappedBy = "table", cascade = CascadeType.PERSIST)
   private List<Booking> bookings = new ArrayList<>();


    @Column(name="table_number", nullable = false, unique = true)
    private int tableNumber;

    @Column(name="Capacity", nullable = false)
    private int capacity;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table( int capacity, int tableNumber){
        this.capacity = capacity;
        this.tableNumber = tableNumber;
    }

    public Table() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
