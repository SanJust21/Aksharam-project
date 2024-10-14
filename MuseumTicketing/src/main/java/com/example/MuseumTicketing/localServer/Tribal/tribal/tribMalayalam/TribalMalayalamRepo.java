package com.example.MuseumTicketing.localServer.Tribal.tribal.tribMalayalam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TribalMalayalamRepo extends JpaRepository<TribalMalayalam,Integer> {
    Optional<TribalMalayalam> findByTribMalUid(String malId);

    Optional<TribalMalayalam> findByTitle(String title);
    Page<TribalMalayalam> findByTribMalUid(String tribMalUid, Pageable pageable);
    List<TribalMalayalam> findBytribMalUid(String malId);
}
