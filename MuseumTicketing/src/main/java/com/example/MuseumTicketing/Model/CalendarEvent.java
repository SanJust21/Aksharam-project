package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "_calendar_event")
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "status")
    private boolean status;

    @Column(name = "totalCapacity")
    private Integer totalCapacity;



    public CalendarEvent() {
    }

    public CalendarEvent(Integer id, LocalTime startTime, Integer capacity,Integer totalCapacity) {
        this.id = id;
        this.startTime = startTime;
        this.capacity = capacity;
        this.totalCapacity=totalCapacity;
    }
}
