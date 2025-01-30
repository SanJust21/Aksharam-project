package com.example.MuseumTicketing.spotReg.userData.foreigner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ForeignerDataRepo extends JpaRepository<ForeignerData,Long> {
    List<ForeignerData> findByVisitDate(LocalDate visitDate);

    List<ForeignerData> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<ForeignerData> findByOrderId(String orderId);

    Optional<ForeignerData> findByTicketId(String ticketId);

    @Query("SELECT MONTH(fd.visitDate) AS month, SUM(fd.grandTotal) AS totalGrandTotal, " +
            "SUM(fd.countOfPeople)AS totalCountOfPeople " +
            "FROM ForeignerData fd WHERE YEAR(fd.visitDate) = :year GROUP BY MONTH(fd.visitDate) ORDER BY month")
    List<Object[]> findMonthlyDataByYear(@Param("year") int year);

    List<ForeignerData> findByPaymentModeAndVisitDate(Integer paymentModeId, LocalDate dateDetails);
}
