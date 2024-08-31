package com.example.MuseumTicketing.tribal.commonId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TribalCommonIdRepo extends JpaRepository<TribalCommonId,Integer> {
    Optional<TribalCommonId> findByCommonId(String commonId);

    Optional<TribalCommonId> findByEnglishId(String tribEngUid);

    Optional<TribalCommonId> findByMalayalamId(String tribMalUid);

    Optional<TribalCommonId> findByMalayalamIdAndEnglishId(String malId, String engId);
}
