package com.example.MuseumTicketing.DTO.Payment;

import lombok.Data;

@Data
public class OrderRequest {

    private double amount;
    private String sessionId;
    private String uniqueId;

}
