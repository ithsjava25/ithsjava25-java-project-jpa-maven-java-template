package org.example.entities;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class OpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    private LocalTime openingTime;
    private LocalTime closingTime;

    public OpeningHours(Long id, Weekday weekday, LocalTime openingTime, LocalTime closingTime) {
        this.id = id;
        this.weekday = weekday;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        if(openingTime.isAfter(closingTime))
            throw new RuntimeException("Opening time cannot be after closing time");
    }

    public OpeningHours(){}

    public Long getId() {
        return id;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
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
            ", weekday='" + weekday + '\'' +
            ", openingTime=" + openingTime +
            ", closingTime=" + closingTime +
            '}';
    }

    enum Weekday{
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }
}
