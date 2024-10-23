package com.example.MuseumTicketing.spotReg.userData.foreigner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ForeignerDataRepo extends JpaRepository<ForeignerData,Long> {
    List<ForeignerData> findByVisitDate(LocalDate visitDate);

    List<ForeignerData> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<ForeignerData> findByOrderId(String orderId);
}
