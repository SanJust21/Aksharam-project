package com.example.MuseumTicketing.spotReg.bookingDetails.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingSpotRepo extends JpaRepository<BookingDetails,Long> {


//    Optional<BookingDetails> findByBookDate(LocalDate bDate);


    Optional<BookingDetails> findByBookDateAndSlotId(LocalDate bDate, Integer slotId);

    List<BookingDetails> findByBookDate(LocalDate bDate);
}
