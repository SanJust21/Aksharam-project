package com.example.MuseumTicketing.appGuide.video.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoMainRepo extends JpaRepository<VideoMain,Integer> {
    List<VideoMain> findBymalId(String mMalUid);

    List<VideoMain> findByengId(String mEngUid);
//    List<VideoMain> findBydtId(String mEngUid);
}
