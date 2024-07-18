package com.example.MuseumTicketing.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "_foreigner_details")
public class ForeignerDetails {


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

        //private String category; // "public" or "foreigner"

        private double totalPrice;

        private LocalDate visitDate;

       // @NotNull
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

        public ForeignerDetails(String mobileNumber, String type, String email, String sessionId, String name,
                                int numberOfAdults, int numberOfChildren, double totalPrice, LocalDate visitDate,
                                String paymentid, String orderId, String ticketId, Integer bookingId, LocalTime slotName,
                                LocalDate bookDate, boolean visitStatus, boolean paymentStatus,String uniqueId) {
            this.mobileNumber = mobileNumber;
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
            this.ticketId = ticketId;
            this.bookDate = bookDate;
            this.bookingId = bookingId;
            this.slotName = slotName;
            this.visitStatus = visitStatus;
            this.paymentStatus = paymentStatus;
            this.uniqueId = uniqueId;
        }

        public ForeignerDetails(Long id) {
            this.id = id;
        }

        public ForeignerDetails() {

        }
}
