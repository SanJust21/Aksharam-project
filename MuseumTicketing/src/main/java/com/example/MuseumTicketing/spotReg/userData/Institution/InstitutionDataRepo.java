package com.example.MuseumTicketing.spotReg.userData.Institution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InstitutionDataRepo extends JpaRepository<InstitutionData,Long> {
    List<InstitutionData> findByVisitDate(LocalDate visitDate);

    List<InstitutionData> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<InstitutionData> findByOrderId(String orderId);

    Optional<InstitutionData> findByTicketId(String ticketId);

    @Query("SELECT MONTH(ind.visitDate) AS month, SUM(ind.grandTotal) AS totalGrandTotal, " +
            "SUM(ind.countOfPeople)AS totalCountOfPeople " +
            "FROM InstitutionData ind WHERE YEAR(ind.visitDate) = :year GROUP BY MONTH(ind.visitDate) ORDER BY month")
    List<Object[]> findMonthlyDataByYear(@Param("year") int year);

    List<InstitutionData> findByPaymentModeAndVisitDate(Integer paymentModeId, LocalDate dateDetails);

    List<InstitutionData> findByVisitDateBetweenAndPaymentMode(LocalDate startDate, LocalDate endDate, Integer paymentModeId);
}
