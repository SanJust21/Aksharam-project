package com.example.MuseumTicketing.onlineTicket.qrCodeData;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "qrUserData")
@Entity
public class QRCodeUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticketId")
    private String ticketId;

    @Column(name = "qrCodeData")
    private byte[] qrCodeData;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

}
