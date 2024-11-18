package com.example.MuseumTicketing.spotReg.bookingDetails.slotData;

import lombok.Data;

import java.time.LocalTime;

@Data
public class SpotSlotGetDto {
    private Integer slotId;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private Integer capacity;
    private Boolean status;
}
