package com.example.MuseumTicketing.appGuide.video.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoFirstRepo extends JpaRepository<VideoFirst,Integer> {
    List<VideoFirst> findByfsMalId(String fsUid);

    List<VideoFirst> findByfsEngId(String fsUid);

    List<VideoFirst> findBydtId(String fsCommonId);
//    List<VideoFirst> findBydtId(String fsUid);
}
