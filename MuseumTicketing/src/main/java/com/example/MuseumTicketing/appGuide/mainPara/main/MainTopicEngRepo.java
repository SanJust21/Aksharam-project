package com.example.MuseumTicketing.appGuide.mainPara.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainTopicEngRepo extends JpaRepository<MainTopicEng,Integer> {
    Optional<MainTopicEng> findByTopic(String topic);

    Optional<MainTopicEng> findBymEngUid(String mEngUid);

//    Optional<MainTopicEng> findByMEngUid(String mEngUid);
}
