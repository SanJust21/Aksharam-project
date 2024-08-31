package com.example.MuseumTicketing.tribal.tribalVideo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TribalVideoRepo extends JpaRepository<TribalVideo,Integer> {
    Optional<TribalVideo> findByCommonId(String commonId);

    Optional<TribalVideo> findByEnglishId(String tribEngUid);

    Optional<TribalVideo> findByMalayalamId(String tribMalUid);

    Optional<TribalVideo> findByCommonIdAndId(String commonId, Integer tId);

    List<TribalVideo> findBycommonId(String commonId);

    List<TribalVideo> findBymalayalamId(String tribMalUid);

    List<TribalVideo> findByenglishId(String tribEngUid);
}
