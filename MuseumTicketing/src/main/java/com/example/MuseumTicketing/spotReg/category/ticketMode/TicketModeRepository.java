package com.example.MuseumTicketing.spotReg.category.ticketMode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketModeRepository extends JpaRepository<TicketModeData,Integer> {
    Optional<TicketModeData> findByName(String name);

    Optional<TicketModeData> findByModeId(String modeId);
}
