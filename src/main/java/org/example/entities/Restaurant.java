package org.example.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "openingHoursId")
    private List<OpeningHours> openingHours = new ArrayList<>();

    private String name;
    private String category;
    private String address;
    private BigDecimal priceRange;
    private double rating;

    public Restaurant(Long id, String name, String category, String address, BigDecimal priceRange, double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.priceRange = priceRange;
        this.rating = rating;
    }

    public Restaurant(){}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(BigDecimal price_range) {
        this.priceRange = price_range;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", category='" + category + '\'' +
            ", address='" + address + '\'' +
            ", priceRange=" + priceRange +
            ", rating=" + rating +
            '}';
    }
}
