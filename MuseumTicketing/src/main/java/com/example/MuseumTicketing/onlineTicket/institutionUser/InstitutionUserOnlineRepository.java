package com.example.MuseumTicketing.onlineTicket.institutionUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InstitutionUserOnlineRepository extends JpaRepository<InstitutionUserOnline,Long> {
    Optional<InstitutionUserOnline> findBySessionId(String orderId);

    Optional<InstitutionUserOnline> findByOrderId(String orderId);

    Optional<InstitutionUserOnline> findByPaymentId(String paymentId);

    Optional<InstitutionUserOnline> findByTicketId(String ticketId);

    List<InstitutionUserOnline> findByBookDateAndSlotId(LocalDate bDate, Integer slotId);

    List<InstitutionUserOnline> findByBookDate(LocalDate currentDate);

    List<InstitutionUserOnline> findByVisitDate(LocalDate bDate);

    List<InstitutionUserOnline> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT MONTH(ind.visitDate) AS month, SUM(ind.grandTotal) AS totalGrandTotal, " +
            "SUM(ind.countOfPeople)AS totalCountOfPeople " +
            "FROM InstitutionUserOnline ind WHERE YEAR(ind.visitDate) = :year GROUP BY MONTH(ind.visitDate) ORDER BY month")
    List<Object[]> findMonthlyDataByYear(@Param("year") int year);
}
