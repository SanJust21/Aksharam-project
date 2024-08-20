package com.example.MuseumTicketing.appGuide.audio.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AudioMainRepo extends JpaRepository<AudioMain,Integer> {
    List<AudioMain> findBydtId(String mEngUid);

    void deleteAllBydtId(String engId);

    Optional<AudioMain> findByCommonId(String commonId);


    Optional<AudioMain> findByDtIdAndId(String malId, Integer ids);
}
