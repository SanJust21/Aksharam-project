package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.ScannedDetails;
import com.example.MuseumTicketing.Model.ScannedDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScannedDetailsRepo extends JpaRepository<ScannedDetails, Long> {
    Optional<ScannedDetails> findByTicketId(String ticketId);

    List<ScannedDetails> findByScannedTimeBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
