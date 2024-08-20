package com.example.MuseumTicketing.appGuide.audio.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AudioFirstRepo extends JpaRepository<AudioFirst,Integer> {
    List<AudioFirst> findBydtId(String fsUid);

    Optional<AudioFirst> findByDtIdAndId(String malId, Integer ids);
}
