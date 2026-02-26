package com.example.MuseumTicketing.onlineTicket.foreginer;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
