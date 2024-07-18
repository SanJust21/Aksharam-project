package com.example.MuseumTicketing.DTO;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DetailsRequest {

    private String sessionId;
    private String uniqueId;

    private String type;
    private String mobileNumber;
    private String email;


    // Fields for school details
    private String institutionName;
    private String district;
    private int numberOfStudents;
    private int numberOfTeachers;
    private LocalDate visitDate;
    private LocalDate bookDate;

    // Fields for public details
    private String name;
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfSeniors;
   // private String category; //'public' or 'foreigner'
    private double totalPrice;
    private String paymentid;

    private boolean visitStatus;
    private String ticketId;
    private boolean paymentStatus;
    private int bookingId;
    private LocalTime slotName;

    public LocalTime getSlotName() {
        return slotName;
    }

    public void setSlotName(LocalTime slotName) {
        this.slotName = slotName;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    private int totalTickets;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(boolean visitStatus) {
        this.visitStatus = visitStatus;
    }

    public LocalDate getBookDate() {
        return bookDate;
    }

    public void setBookDate(LocalDate bookDate) {
        this.bookDate = bookDate;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }

//    public String getCategory() {
//        return category;
//    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setCategory(String category) {
//        this.category = category;
//    }


    public int getNumberOfSeniors() {
        return numberOfSeniors;
    }

    public void setNumberOfSeniors(int numberOfSeniors) {
        this.numberOfSeniors = numberOfSeniors;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getType() {
        return type;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getDistrict() {
        return district;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public int getNumberOfTeachers() {
        return numberOfTeachers;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public String getEmail() {
        return email;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public void setNumberOfTeachers(int numberOfTeachers) {
        this.numberOfTeachers = numberOfTeachers;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
