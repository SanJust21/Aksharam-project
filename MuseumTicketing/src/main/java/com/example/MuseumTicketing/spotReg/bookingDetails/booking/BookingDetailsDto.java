package com.example.MuseumTicketing.spotReg.bookingDetails.booking;

import lombok.Data;

import java.time.LocalTime;

@Data
public class BookingDetailsDto {
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private Integer presentCapacity;
    private Boolean presentStatus;
}
