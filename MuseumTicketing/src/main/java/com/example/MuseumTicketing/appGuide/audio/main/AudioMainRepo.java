package com.example.MuseumTicketing.appGuide.audio.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioMainRepo extends JpaRepository<AudioMain,Integer> {
    List<AudioMain> findBydtId(String mEngUid);
}
