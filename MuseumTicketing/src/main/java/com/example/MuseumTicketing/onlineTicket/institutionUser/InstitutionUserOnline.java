package com.example.MuseumTicketing.onlineTicket.institutionUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "institutionOnlineTable")
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionUserOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phNumber")
    private String phNumber;

    @Column(name = "district")
    private String district;

    @Column(name = "teacher")
    private Integer teacher;

    @Column(name = "student")
    private Integer student;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "visitDate")
    private LocalDate visitDate;

    @Column(name = "bookDate")
    private LocalDate bookDate;

    @Column(name = "totalAmount")
    private Double totalAmount;

    @Column(name = "totalGstCharge")
    private Double totalGstCharge;

    @Column(name = "teacherTicketCharge")
    private Double teacherTicketCharge;

    @Column(name = "studentTicketCharge")
    private Double studentTicketCharge;

    @Column(name = "grandTotal")
    private Double grandTotal;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "paymentId")
    private String paymentId;

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

    @Column(name = "sessionId")
    private String sessionId;

    @Column(name = "createdAt")
    private LocalTime createdAt;

}
