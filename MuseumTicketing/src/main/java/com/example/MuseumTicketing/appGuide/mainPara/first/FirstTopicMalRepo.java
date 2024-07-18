package com.example.MuseumTicketing.appGuide.mainPara.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FirstTopicMalRepo extends JpaRepository<FirstTopicMal,Integer> {
    Optional<FirstTopicMal> findByTopic(String topic);

    Optional<FirstTopicMal> findByFsUid(String malId);


    List<FirstTopicMal> findByMainUid(String mainId);
}
