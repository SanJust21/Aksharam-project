package com.example.MuseumTicketing.onlineTicket.otpData;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Table(name = "otpDataTable")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OTPTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "otpValue")
    private String otpValue;

    @Column(name = "genTime")
    private LocalDateTime genTime;
}
