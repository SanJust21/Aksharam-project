package com.example.MuseumTicketing.spotReg.bookingDetails.slotData;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "spotSlot")
@CrossOrigin
public class SpotSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "slotStartTime")
    private LocalTime slotStartTime;

    @Column(name = "slotEndTime")
    private LocalTime slotEndTime;

    @Column(name = "totalCapacity")
    private Integer totalCapacity;

    @Column(name = "status")
    private Boolean status;
}
