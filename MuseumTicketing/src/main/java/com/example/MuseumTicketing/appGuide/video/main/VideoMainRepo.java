package com.example.MuseumTicketing.appGuide.video.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoMainRepo extends JpaRepository<VideoMain,Integer> {
    List<VideoMain> findBymalId(String mMalUid);

    List<VideoMain> findByengId(String mEngUid);

    Optional<VideoMain> findBydtId(String commonId);

//    Optional<VideoMain> findByDtIdAndMalId(String commonId, String malId);
//
//    Optional<VideoMain> findByDtIdAndEngId(String commonId, String malId);

    Optional<VideoMain> findByDtIdAndId(String commonId, Integer ids);
//    List<VideoMain> findBydtId(String mEngUid);
}
