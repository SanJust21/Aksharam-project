package com.example.MuseumTicketing.tribal.tribEnglish;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TribalEnglishRepo extends JpaRepository<TribalEnglish,Integer> {
    Optional<TribalEnglish> findByTribEngUid(String engId);

    Optional<TribalEnglish> findByTitle(String title);

    List<TribalEnglish> findBytribEngUid(String engId);
}
