package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "_institution_details")
public class InstitutionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mobileNumber;

    private String type;

    private String email;

    private String sessionId;

    private String institutionName;

    private String district;

    private int numberOfStudents;

    private int numberOfTeachers;

    private LocalDate visitDate;

    //@NotNull
    private LocalDate bookDate;

    private double totalPrice;

    private String orderId;

    private String paymentid;

    private String ticketId;

     private Integer bookingId;

     private LocalTime slotName;

    @Column(name = "visit_status", nullable = false, columnDefinition = "boolean default false")
    private boolean visitStatus;

    @Column(name = "payment_status", nullable = false, columnDefinition = "boolean default false")
    private boolean paymentStatus;

    @Column(name = "uniqueId")
    private String uniqueId;

    public InstitutionDetails(String mobileNumber, String type, String email, String sessionId, String institutionName,
                              String district, int numberOfStudents, int numberOfTeachers, LocalDate visitDate,
                              double totalPrice, String orderId, String paymentid, String ticketId, LocalDate bookDate,
                              Integer bookingId,LocalTime slotName, boolean visitStatus, boolean paymentStatus,
                              String uniqueId) {
        this.mobileNumber = mobileNumber;
        this.uniqueId = uniqueId;
        this.type = type;
        this.email = email;
        this.sessionId = sessionId;
        this.institutionName = institutionName;
        this.district = district;
        this.numberOfStudents = numberOfStudents;
        this.numberOfTeachers = numberOfTeachers;
        this.visitDate = visitDate;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.paymentid = paymentid;
        this.ticketId = ticketId;
        this.bookDate = bookDate;
        this.bookingId = bookingId;
        this.slotName = slotName;
        this.visitStatus = visitStatus;
        this.paymentStatus = paymentStatus;
    }

    public InstitutionDetails(Long id) {
        this.id = id;
    }

    public InstitutionDetails() {

    }
}

