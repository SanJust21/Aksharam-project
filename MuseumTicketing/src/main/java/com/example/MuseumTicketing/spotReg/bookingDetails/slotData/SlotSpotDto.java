package com.example.MuseumTicketing.spotReg.bookingDetails.slotData;

import lombok.Data;

import java.time.LocalTime;

@Data
public class SlotSpotDto {
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private Integer capacity;
    private Boolean status;
}
