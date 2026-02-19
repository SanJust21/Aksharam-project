package com.example.MuseumTicketing.onlineTicket.foreginer;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "foreignerUserOnlineTable")
public class ForeignerUserOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phNumber")
    private String phNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "sessionId")
    private String sessionId;

    @Column(name = "adult")
    private Integer adult;

    @Column(name = "child")
    private Integer child;

    @Column(name = "adultGrandTotal")
    private Double adultGrandTotal;

    @Column(name = "childGrandTotal")
    private Double childGrandTotal;


    @Column(name = "totalAmount")
    private Double totalAmount;

    @Column(name = "totalGstCharge")
    private Double totalGstCharge;

    @Column(name = "grandTotal")
    private Double grandTotal;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "paymentId")
    private String paymentId;

    @Column(name = "visitDate")
    private LocalDate visitDate;

    @Column(name = "bookDate")
    private LocalDate bookDate;

    @Column(name = "ticketId")
    private String ticketId;

    @Column(name = "bookingId")
    private Integer bookingId;

    @Column(name = "slotId")
    private Integer slotId;

    @Column(name = "slotName")
    private LocalTime slotName;

    @Column(name = "visit_status", nullable = false, columnDefinition = "boolean default false")
    private boolean visitStatus;

    @Column(name = "payment_status", nullable = false, columnDefinition = "boolean default false")
    private boolean paymentStatus;

    @Column(name = "countOfPeople")
    private Integer countOfPeople;

    @Column(name = "createdAt")
    private LocalTime createdAt;
}
