package com.example.MuseumTicketing.spotReg.bookingDetails.booking;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "book_spot_data")
public class BookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bookDate")
    private LocalDate bookDate;

    @Column(name = "slotId")
    private Integer slotId;

    @Column(name = "slotStartTime")
    private LocalTime slotStartTime;

    @Column(name = "slotEndTime")
    private LocalTime slotEndTime;

    @Column(name = "presentCapacity")
    private Integer presentCapacity;

    @Column(name = "totalCapacity")
    private Integer totalCapacity;

    @Column(name = "presentStatus")
    private Boolean presentStatus;
}
