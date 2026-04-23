package com.example.MuseumTicketing.onlineTicket.publicUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PublicUserOnlineRepository extends JpaRepository<PublicUserOnline,Long> {
    Optional<PublicUserOnline> findBySessionId(String orderId);

    Optional<PublicUserOnline> findByOrderId(String orderId);

    Optional<PublicUserOnline> findByPaymentId(String paymentId);

    Optional<PublicUserOnline> findByTicketId(String ticketId);

    List<PublicUserOnline> findByBookDateAndSlotId(LocalDate bDate, Integer slotId);

    List<PublicUserOnline> findByBookDate(LocalDate currentDate);


    List<PublicUserOnline> findByVisitDate(LocalDate bDate);

    List<PublicUserOnline> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT MONTH(pd.visitDate) AS month, SUM(pd.grandTotal) AS totalGrandTotal, " +
            "SUM(pd.countOfPeople)AS totalCountOfPeople " +
            "FROM PublicUserOnline pd WHERE YEAR(pd.visitDate) = :year GROUP BY MONTH(pd.visitDate) ORDER BY month")
    List<Object[]> findMonthlyDataByYear(@Param("year") int year);
}
