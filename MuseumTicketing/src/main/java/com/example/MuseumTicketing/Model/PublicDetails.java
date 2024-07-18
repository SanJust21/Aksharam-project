package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "_public_details")
public class PublicDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mobileNumber;

    private String type;

    private String email;

    private String sessionId;

    private String name;

    private int numberOfAdults;

    private int numberOfChildren;

    private int numberOfSeniors;

    //private String category; // "public" or "foreigner"

    private double totalPrice;

    private LocalDate visitDate;

    //@NotNull
    private LocalDate bookDate;

    private String paymentid;

    private String orderId;

    private String ticketId;

     private Integer bookingId;

     private LocalTime slotName;

    @Column(name = "visit_status", nullable = false, columnDefinition = "boolean default false")
    private boolean visitStatus;

    @Column(name = "payment_status", nullable = false, columnDefinition = "boolean default false")
    private boolean paymentStatus;
    @Column(name = "uniqueId")
    private String uniqueId;

    public PublicDetails(String mobileNumber, String type, String email, String sessionId, String name,
                         int numberOfAdults, int numberOfChildren, int numberOfSeniors,
                         double totalPrice, LocalDate visitDate, String paymentid, String orderId, String ticketId,
                         LocalDate bookDate, Integer bookingId, LocalTime slotName, boolean visitStatus,
                         boolean paymentStatus, String uniqueId) {
        this.mobileNumber = mobileNumber;
        this.uniqueId = uniqueId;
        this.type = type;
        this.email = email;
        this.sessionId = sessionId;
        this.name = name;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
        this.totalPrice = totalPrice;
        this.visitDate = visitDate;
        this.paymentid = paymentid;
        this.orderId = orderId;
        this.numberOfSeniors = numberOfSeniors;
        this.ticketId = ticketId;
        this.bookDate = bookDate;
        this.bookingId = bookingId;
        this.slotName = slotName;
        this.visitStatus = visitStatus;
        this.paymentStatus = paymentStatus;
    }

    public PublicDetails(Long id) {
        this.id = id;
    }

    public PublicDetails() {

    }
}

