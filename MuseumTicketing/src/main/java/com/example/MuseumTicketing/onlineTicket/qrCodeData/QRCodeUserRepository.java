package com.example.MuseumTicketing.onlineTicket.qrCodeData;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeUserRepository extends JpaRepository<QRCodeUserData,Long> {
}
