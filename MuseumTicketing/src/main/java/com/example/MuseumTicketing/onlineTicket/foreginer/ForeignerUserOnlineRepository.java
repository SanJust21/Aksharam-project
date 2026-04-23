package com.example.MuseumTicketing.onlineTicket.foreginer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ForeignerUserOnlineRepository extends JpaRepository<ForeignerUserOnline,Long> {
    Optional<ForeignerUserOnline> findBySessionId(String orderId);

    Optional<ForeignerUserOnline> findByOrderId(String orderId);

    Optional<ForeignerUserOnline> findByPaymentId(String paymentId);

    Optional<ForeignerUserOnline> findByTicketId(String ticketId);

    List<ForeignerUserOnline> findByBookDateAndSlotId(LocalDate bDate, Integer slotId);

    List<ForeignerUserOnline> findByBookDate(LocalDate currentDate);

    List<ForeignerUserOnline> findByVisitDate(LocalDate bDate);

    List<ForeignerUserOnline> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT MONTH(fd.visitDate) AS month, SUM(fd.grandTotal) AS totalGrandTotal, " +
            "SUM(fd.countOfPeople)AS totalCountOfPeople " +
            "FROM ForeignerUserOnline fd WHERE YEAR(fd.visitDate) = :year GROUP BY MONTH(fd.visitDate) ORDER BY month")
    List<Object[]> findMonthlyDataByYear(@Param("year") int year);
}
