package com.example.MuseumTicketing.appGuide.mainPara.qrCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonQRParaIdRepo extends JpaRepository<CommonQRParaId,Integer> {


    boolean existsByMalIdAndEngId(String mMalUid, String mEngUid);


    Optional<CommonQRParaId> findByMalIdAndEngId(String mMalUid, String mEngUid);

    Optional<CommonQRParaId> findByCommonId(String commonId);

    Optional<CommonQRParaId> findByEngId(String mEngUid);

    Optional<CommonQRParaId> findByMalId(String mMalUid);
}
