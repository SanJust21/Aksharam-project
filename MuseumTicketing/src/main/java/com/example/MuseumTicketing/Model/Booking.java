package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bookingId")
    private Integer bookingId;

    @Column(name = "available")
    private boolean available;

    @Column(name = "tickets")
    private Integer tickets;

    @Column(name = "category")
    private String category;

    @Column(name = "expireTime")
    private LocalDateTime expireTime;

    @Column(name = "bookTime")
    private LocalDateTime bookTime;

    @Column(name = "visitDate")
    private LocalDate visitDate;

    @Column(name = "slotName")
    private LocalTime slotName;
}