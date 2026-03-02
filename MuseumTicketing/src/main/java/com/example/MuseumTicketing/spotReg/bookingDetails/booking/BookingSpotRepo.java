package com.example.MuseumTicketing.spotReg.bookingDetails.booking;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingSpotRepo extends JpaRepository<BookingDetails,Long> {


//    Optional<BookingDetails> findByBookDate(LocalDate bDate);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT b FROM BookingDetails b WHERE b.bookDate = :visitDate AND b.slotId = :slotId
            """)
    Optional<BookingDetails> findByBookDateAndSlotIdForUpdate(@Param("visitDate") LocalDate bDate, @Param("slotId") Integer slotId);

    List<BookingDetails> findByBookDate(LocalDate bDate);

    @Transactional
    void deleteByBookDateBefore(LocalDate now);
}
