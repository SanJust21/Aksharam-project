package com.example.MuseumTicketing.spotReg.userData.Institution;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InstitutionDataRepo extends JpaRepository<InstitutionData,Long> {
    List<InstitutionData> findByVisitDate(LocalDate visitDate);

    List<InstitutionData> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<InstitutionData> findByOrderId(String orderId);
}
