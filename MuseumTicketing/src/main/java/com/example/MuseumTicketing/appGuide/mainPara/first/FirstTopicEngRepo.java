package com.example.MuseumTicketing.appGuide.mainPara.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FirstTopicEngRepo extends JpaRepository<FirstTopicEng,Integer> {
    Optional<FirstTopicEng> findByTopic(String topic);

    Optional<FirstTopicEng> findByfsUid(String engId);


    List<FirstTopicEng> findByMainUid(String mainId);
}
