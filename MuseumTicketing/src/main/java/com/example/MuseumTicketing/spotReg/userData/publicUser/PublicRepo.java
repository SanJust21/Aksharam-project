package com.example.MuseumTicketing.spotReg.userData.publicUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PublicRepo extends JpaRepository<PublicData,Long> {
    List<PublicData> findByVisitDate(LocalDate visitDate);

    List<PublicData> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<PublicData> findByOrderId(String orderId);

    List<Object[]> countSlotIdByVisitDate(LocalDate dateData);
}
