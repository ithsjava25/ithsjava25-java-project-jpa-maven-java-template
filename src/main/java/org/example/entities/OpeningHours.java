package org.example.entities;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class OpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String weekday;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public OpeningHours(Long id, Restaurant restaurant, String weekday, LocalTime openingTime, LocalTime closingTime) {
        this.id = id;
        this.restaurant = restaurant;
        this.weekday = weekday;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public OpeningHours(){}

    public Long getId() {
        return id;
    }

    public String getWeekday() {
        return weekday;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime opening_time) {
        this.openingTime = opening_time;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closing_time) {
        this.closingTime = closing_time;
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
            "id=" + id +
            ", restaurant=" + restaurant +
            ", weekday='" + weekday + '\'' +
            ", openingTime=" + openingTime +
            ", closingTime=" + closingTime +
            '}';
    }
}
