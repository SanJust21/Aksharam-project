package com.example.MuseumTicketing.onlineTicket.foreginer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForeignerUserOnlineRepository extends JpaRepository<ForeignerUserOnline,Long> {
    Optional<ForeignerUserOnline> findBySessionId(String orderId);

    Optional<ForeignerUserOnline> findByOrderId(String orderId);

    Optional<ForeignerUserOnline> findByPaymentId(String paymentId);

    Optional<ForeignerUserOnline> findByTicketId(String ticketId);
}
