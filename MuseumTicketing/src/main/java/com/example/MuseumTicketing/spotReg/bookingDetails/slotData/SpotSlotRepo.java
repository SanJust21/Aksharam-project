package com.example.MuseumTicketing.spotReg.bookingDetails.slotData;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Optional;

public interface SpotSlotRepo extends JpaRepository<SpotSlot,Integer> {
    Optional<SpotSlot> findBySlotStartTimeAndSlotEndTime(LocalTime sStartTime, LocalTime sEndTime);
}
