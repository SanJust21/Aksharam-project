package com.example.MuseumTicketing.appGuide.mainPara.qrCode.first;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubComIdRepo extends JpaRepository<SubComId,Integer> {
    Optional<SubComId> findByFsCommonId(String commonId);

    SubComId findByfsEngId(String fsUid);

    SubComId findByfsMalId(String fsUid);

    Optional<SubComId> findByFsEngIdAndFsMalId(String engId, String malId);

//    SubComId findByFsEngIdAndFsMalId(String engId, String malId);
}
