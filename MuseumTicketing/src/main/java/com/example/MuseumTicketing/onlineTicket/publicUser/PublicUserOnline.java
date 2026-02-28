package com.example.MuseumTicketing.onlineTicket.publicUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "publicOnlineTable")
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phNumber")
    private String phNumber;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "sessionId")
    private String sessionId;

    @Column(name = "name")
    private String name;

    @Column(name = "adult")
    private Integer adult;

    @Column(name = "child")
    private Integer child;

//    @Column(name = "student")
//    private Integer student;

    @Column(name = "adultGrandTotal")
    private Double adultGrandTotal;

    @Column(name = "childGrandTotal")
    private Double childGrandTotal;

//    @Column(name = "studentGrandTotal")
//    private Double studentGrandTotal;


    @Column(name = "totalAmount")
    private Double totalAmount;

    @Column(name = "totalGstCharge")
    private Double totalGstCharge;

    @Column(name = "grandTotal")
    private Double grandTotal;

    @Column(name = "visitDate")
    private LocalDate visitDate;

    @Column(name = "bookDate")
    private LocalDate bookDate;

    @Column(name = "paymentId")
    private String paymentId;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "ticketId")
    private String ticketId;

    @Column(name = "bookingId")
    private String bookingId;

    @Column(name = "slotId")
    private Integer slotId;

    @Column(name = "slotStartTime")
    private LocalTime slotStartTime;

    @Column(name = "slotEndTime")
    private LocalTime slotEndTime;

    @Column(name = "visit_status", nullable = false, columnDefinition = "boolean default false")
    private boolean visitStatus;

    @Column(name = "payment_status", nullable = false, columnDefinition = "boolean default false")
    private boolean paymentStatus;

    @Column(name = "countOfPeople")
    private Integer countOfPeople;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

}
