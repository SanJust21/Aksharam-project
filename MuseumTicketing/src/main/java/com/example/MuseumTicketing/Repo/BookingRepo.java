package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.Booking;
import com.example.MuseumTicketing.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {
    Booking findFirstByBookingIdAndAvailableTrue(Integer bookingId);

    List<Booking> findByExpireTimeBefore(LocalDateTime now);

    Booking findByBookingId(Integer bId);

    List<Booking> findByVisitDate(LocalDate date);
}
