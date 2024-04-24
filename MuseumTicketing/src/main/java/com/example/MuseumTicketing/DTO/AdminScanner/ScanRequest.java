package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ScanRequest {

    private String ticketId;

    private String scannedTime;
}
