package com.example.MuseumTicketing.onlineTicket.institutionUser;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
