package com.example.MuseumTicketing.Service.Slot;

import com.example.MuseumTicketing.Model.*;
import com.example.MuseumTicketing.Repo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j

public class BookingService {
    @Autowired
    private CalendarRepo calendarRepo;
    @Autowired
    private PublicDetailsRepo publicDetailsRepo;
    @Autowired
    private InstitutionDetailsRepo institutionDetailsRepo;
    @Autowired
    private ForeignerDetailsRepo foreignerDetailsRepo;
    @Autowired
    private BookingRepo bookingRepo;

    public Integer lockCapacity(Integer capacity, LocalDate visitDate, LocalTime slotName, String category) {
        CalendarEvent calendarEvent = calendarRepo.findByCapacityGreaterThanEqualAndStartDateAndStartTime(capacity, visitDate, slotName);
        log.info("lockCapacity" + calendarEvent);

        if (calendarEvent == null) {
            throw new IllegalArgumentException("Capacity is Overflowing ");
        }
        else {

            Booking booking1 = new Booking();

            booking1.setCategory(category);
            booking1.setTickets(capacity);
            booking1.setVisitDate(visitDate);
            booking1.setSlotName(slotName);
            booking1.setBookTime(LocalDateTime.now());
            booking1.setExpireTime(booking1.getBookTime().plusMinutes(15)); //plusMinutes(2)
            booking1.setAvailable(true);
            bookingRepo.save(booking1);

            if ("institution".equals(booking1.getCategory())) {

                InstitutionDetails institutionDetails = new InstitutionDetails();

                institutionDetails.setType(category);
                institutionDetails.setBookingId(booking1.getBookingId());
                institutionDetails.setVisitDate(booking1.getVisitDate());
                institutionDetails.setSlotName(booking1.getSlotName());
                institutionDetailsRepo.save(institutionDetails);
            }
            else if ("public".equals(booking1.getCategory())) {

                PublicDetails publicDetails = new PublicDetails();

                publicDetails.setType(booking1.getCategory());
                publicDetails.setBookingId(booking1.getBookingId());
                publicDetails.setVisitDate(booking1.getVisitDate());
                publicDetails.setSlotName(booking1.getSlotName());
                publicDetailsRepo.save(publicDetails);
            }
            else {

                ForeignerDetails foreignerDetails = new ForeignerDetails();

                foreignerDetails.setType(booking1.getCategory());
                foreignerDetails.setBookingId(booking1.getBookingId());
                foreignerDetails.setVisitDate(booking1.getVisitDate());
                foreignerDetails.setSlotName(booking1.getSlotName());
                foreignerDetailsRepo.save(foreignerDetails);
                }
                log.info("Inside " + booking1);
                return booking1.getBookingId();
            }
        }

    public Integer confirmBooking(Integer BookingId){

        Booking booking = bookingRepo.findFirstByBookingIdAndAvailableTrue(BookingId);
        log.info("available : "+booking.isAvailable());
        log.info(" bookingId : "+booking.getBookingId());

        if (booking!=null && booking.isAvailable()) {

            booking.setAvailable(false);
            bookingRepo.save(booking);
            Integer cap = booking.getTickets();
            LocalDate staDate = booking.getVisitDate();
            LocalTime sName = booking.getSlotName();

            CalendarEvent calendarEvent1 = calendarRepo.findByCapacityGreaterThanEqualAndStartDateAndStartTime(cap,staDate,sName);

            if (calendarEvent1!=null)
            {
                Integer capa= calendarEvent1.getCapacity();
                log.info("capacity :"+ capa);
                log.info("capacity :"+ cap);
                calendarEvent1.setCapacity(capa-cap);
                log.info("new capacity: "+calendarEvent1.getCapacity());
                calendarRepo.save(calendarEvent1);
            }
        return booking.getBookingId();
    }
    else {
        throw new IllegalStateException("Booking already confirmed");
    }
}
}