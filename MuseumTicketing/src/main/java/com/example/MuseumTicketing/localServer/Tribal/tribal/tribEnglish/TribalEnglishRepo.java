package com.example.MuseumTicketing.localServer.Tribal.tribal.tribEnglish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TribalEnglishRepo extends JpaRepository<TribalEnglish,Integer> {
    Optional<TribalEnglish> findByTribEngUid(String engId);

    Optional<TribalEnglish> findByTitle(String title);

    Page<TribalEnglish> findByTribEngUid(String engId, Pageable pageable);
}
