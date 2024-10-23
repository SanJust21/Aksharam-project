package com.example.MuseumTicketing.spotReg.userData;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SpotPaymentDto {
    private Integer paymentStatusId;
    private LocalDate visitDate;
    private Integer slotId;
    private String createdBy;
}
