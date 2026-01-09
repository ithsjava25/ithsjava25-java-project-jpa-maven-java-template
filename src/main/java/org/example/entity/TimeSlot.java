package org.example.entity;

import jakarta.persistence.*;

@Entity
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String startTime;

    @Column(nullable = false, unique = true)
    private String finishTime;

    public TimeSlot(String startTime, String finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public TimeSlot() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
            "id=" + id +
            ", startTime='" + startTime + '\'' +
            ", finishTime='" + finishTime + '\'' +
            '}';
    }
}
