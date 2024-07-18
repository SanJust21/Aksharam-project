package com.example.MuseumTicketing.appGuide.mainPara.main;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainTopicMalRepo extends JpaRepository<MainTopicMal,Integer> {
    Optional<MainTopicMal> findByTopic(String topic);

    //Optional<MainTopicMal> findByMMalUid(String mMalUid);

    Optional<MainTopicMal> findBymMalUid(String mMalUid);
}
