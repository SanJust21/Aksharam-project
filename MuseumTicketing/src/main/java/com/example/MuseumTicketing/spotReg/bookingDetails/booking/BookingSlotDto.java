package com.example.MuseumTicketing.spotReg.bookingDetails.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSlotDto {
    private Integer slotId;
    private LocalDate bookDate;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private Integer presentCapacity;
    private Integer totalCapacity;
    private Boolean presentStatus;
}
