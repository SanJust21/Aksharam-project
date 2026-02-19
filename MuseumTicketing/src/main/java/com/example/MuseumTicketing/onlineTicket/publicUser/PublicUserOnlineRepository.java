package com.example.MuseumTicketing.onlineTicket.publicUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicUserOnlineRepository extends JpaRepository<PublicUserOnline,Long> {
    Optional<PublicUserOnline> findBySessionId(String orderId);

    Optional<PublicUserOnline> findByOrderId(String orderId);

    Optional<PublicUserOnline> findByPaymentId(String paymentId);

    Optional<PublicUserOnline> findByTicketId(String ticketId);
}
