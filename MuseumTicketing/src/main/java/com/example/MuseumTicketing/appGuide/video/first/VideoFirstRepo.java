package com.example.MuseumTicketing.appGuide.video.first;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface VideoFirstRepo extends JpaRepository<VideoFirst,Integer> {
    List<VideoFirst> findByfsMalId(String fsUid);

    List<VideoFirst> findByfsEngId(String fsUid);

    List<VideoFirst> findBydtId(String fsCommonId);

    Optional<VideoFirst> findByDtIdAndId(String commonId, Integer ids);


//    List<VideoFirst> findBydtId(String fsUid);
}
