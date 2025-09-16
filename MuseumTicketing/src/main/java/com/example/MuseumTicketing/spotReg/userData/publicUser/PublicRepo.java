package com.example.MuseumTicketing.spotReg.userData.publicUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PublicRepo extends JpaRepository<PublicData,Long> {
    List<PublicData> findByVisitDate(LocalDate visitDate);

    List<PublicData> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<PublicData> findByOrderId(String orderId);



    Optional<PublicData> findByTicketId(String ticketId);

    @Query("SELECT MONTH(pd.visitDate) AS month, SUM(pd.grandTotal) AS totalGrandTotal, " +
            "SUM(pd.countOfPeople)AS totalCountOfPeople " +
            "FROM PublicData pd WHERE YEAR(pd.visitDate) = :year GROUP BY MONTH(pd.visitDate) ORDER BY month")
    List<Object[]> findMonthlyDataByYear(@Param("year") int year);


    List<PublicData> findByPaymentModeAndVisitDate(Integer paymentModeId, LocalDate dateDetails);

    List<PublicData> findByVisitDateBetweenAndPaymentMode(LocalDate startDate, LocalDate endDate, Integer paymentId);
}
